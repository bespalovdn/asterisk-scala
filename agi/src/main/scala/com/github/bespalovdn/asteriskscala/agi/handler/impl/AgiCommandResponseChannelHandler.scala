package com.github.bespalovdn.asteriskscala.agi.handler.impl

import com.github.bespalovdn.asteriskscala.agi.command.AgiCommand
import com.github.bespalovdn.asteriskscala.agi.command.response.{AgiResponse, FailResponse}
import com.github.bespalovdn.asteriskscala.agi.handler.{AgiHandler, ChannelHandlerContextProvider}
import com.github.bespalovdn.scalalog.LoggerSupport
import io.netty.channel.{ChannelFuture, ChannelFutureListener, ChannelHandlerContext, SimpleChannelInboundHandler}

import scala.concurrent.{Future, Promise}

private [handler]
abstract class AgiCommandResponseChannelHandler extends SimpleChannelInboundHandler[AgiResponse]
    with AgiHandler
    with LoggerSupport
{
    def contextProvider: ChannelHandlerContextProvider

    override def send(command: AgiCommand): Future[AgiResponse] = {
        logger.trace("Sending AGI command: " + command)
        responsePromise = Some(Promise[AgiResponse])
        contextProvider.context.writeAndFlush(command).addListener(new ChannelFutureListener {
            override def operationComplete(future: ChannelFuture): Unit = {
                if(!future.isSuccess)
                    responsePromise.foreach{_.failure(new Exception("Failed to send AgiCommand: " + command))}
            }
        })
        responsePromise.get.future
    }

    override def channelRead0(ctx: ChannelHandlerContext, msg: AgiResponse): Unit = {
        logger.trace("Received AGI response: " + msg)
        responsePromise.foreach{promise => msg match {
            case response: FailResponse => promise.failure(response)
            case response: AgiResponse => promise.success(response)
        }}
        responsePromise = None
    }

    private var responsePromise: Option[Promise[AgiResponse]] = None
}
