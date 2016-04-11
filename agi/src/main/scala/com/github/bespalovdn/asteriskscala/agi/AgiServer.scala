package com.github.bespalovdn.asteriskscala.agi

import java.net.InetSocketAddress

import com.github.bespalovdn.asteriskscala.agi.execution.AsyncAction
import com.github.bespalovdn.asteriskscala.agi.future.FutureConversions
import com.github.bespalovdn.asteriskscala.agi.handler.AgiRequestHandlerFactory
import com.github.bespalovdn.scalalog.StaticLogger
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel._
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel

import scala.concurrent.{Future, Promise}

class AgiServer(bindAddr: InetSocketAddress, handlerFactory: AgiRequestHandlerFactory)
    extends AsyncAction
    with FutureConversions
    with StaticLogger
{
    def run(): LifeCycle = {
        logger.info("Starting server on %s...".format(bindAddr))

        val parentGroup: EventLoopGroup = new NioEventLoopGroup
        val childGroup: EventLoopGroup = new NioEventLoopGroup

        val bootstrap = new ServerBootstrap()
        bootstrap.
            group(parentGroup, childGroup).
            channel(classOf[NioServerSocketChannel]).
            childHandler(new ChannelInitializer[SocketChannel] {
                override def initChannel(ch: SocketChannel): Unit =
                    handlerFactory.createHandler().initializeChannel(ch)//create the handler and initialize them
            }).
            option[java.lang.Integer](ChannelOption.SO_BACKLOG, 256).
            childOption[java.lang.Boolean](ChannelOption.SO_KEEPALIVE, true)

        // bind and start to accept incoming connections:
        val channelPromise = Promise[Channel]()
        bootstrap.bind(bindAddr).addListener(new ChannelFutureListener {
            override def operationComplete(future: ChannelFuture): Unit = {
                if(future.isSuccess)
                    channelPromise.success(future.channel())
                else
                    channelPromise.failure(future.cause())
            }
        })
        def cleanup(): Future[Unit] = {
            val f1 = parentGroup.shutdownGracefully().asScala
            val f2 = childGroup.shutdownGracefully().asScala
            val fCleanup = f1 >> f2
            fCleanup onComplete {_ => logger.info("Server stopped.")}
            fCleanup
        }
        val lifeCycle = new LifeCycle(channelPromise.future, cleanup)
        lifeCycle.started >> Future{logger.info("Server started.")}
        lifeCycle
    }

    /**
     * Represents this server's lifetime.
 *
     * @param channel Represents connection channel in terms of Netty.
     * @param cleanup The cleanup function, which should be called when channel is closed.
     */
    class LifeCycle(channel: Future[Channel], cleanup: () => Future[Unit])
    {
        private lazy val stopRequest: Future[Unit] = (channel >>= {_.close().asScala}) >> stopped

        /**
         * Returns `started` future, which completes when server started.
         */
        lazy val started: Future[Unit] = channel >> ().point[Future]

        /**
         * Returns `stopped` future, which completes when server stopped.
         */
        lazy val stopped: Future[Unit] = channel >>= {_.closeFuture().asScala}

        // schedule cleanup:
        stopped onComplete {_ => cleanup()}

        /**
         * Ask the server to stop.
 *
         * @return Future which completes when server stopped.
         */
        def stop(): Future[Unit] = stopRequest
    }
}
