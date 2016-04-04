package com.github.bespalovdn.asteriskscala.agi

import java.net.InetSocketAddress

import com.github.bespalovdn.asteriskscala.agi.execution.AsyncActionSupport
import com.github.bespalovdn.asteriskscala.agi.handler.AgiRequestHandlerFactory
import com.github.bespalovdn.asteriskscala.common.concurrent.FutureConversions
import com.github.bespalovdn.scalalog.StaticLogger
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel._
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel

import scala.concurrent.{Future, Promise}

class AgiServer(bindAddr: InetSocketAddress, handlerFactory: AgiRequestHandlerFactory)
    extends AsyncActionSupport
    with FutureConversions
    with StaticLogger
{
    def run(): LifeTime = {
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
            val cleanup = f1 >> f2
            cleanup onComplete {_ => logger.info("Server stopped.")}
            cleanup >> ().toFuture
        }
        val lifetime = new LifeTime(channelPromise.future, cleanup)
        lifetime.started onComplete {_ => logger.info("Server started.")}
        lifetime
    }

    /**
     * Represents this server's lifetime.
 *
     * @param channel Represents connection channel in terms of Netty.
     * @param cleanup The cleanup function, which should be called when channel is closed.
     */
    class LifeTime(channel: Future[Channel], cleanup: () => Future[Unit])
    {
        /**
         * Returns `started` future, which completes when server started.
         */
        val started: Future[Unit] = channel >> ().toFuture

        /**
         * Returns `stopped` future, which completes when server stopped.
         */
        val stopped: Future[Unit] = {channel >>= {_.closeFuture().asScala}} >> ().toFuture

        stopped onComplete {_ => cleanup()}

        /**
         * Ask the server to stop.
 *
         * @return Future which completes when server stopped.
         */
        def stop(): Future[Unit] = (channel >>= {_.close().asScala}) >> stopped
    }
}
