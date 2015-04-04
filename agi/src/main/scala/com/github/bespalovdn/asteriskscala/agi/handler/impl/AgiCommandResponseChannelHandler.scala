package com.github.bespalovdn.asteriskscala.agi.handler.impl

import com.github.bespalovdn.asteriskscala.agi.command.AgiCommand
import com.github.bespalovdn.asteriskscala.agi.handler.{AgiCommandSender, ChannelHandlerContextProvider}
import com.github.bespalovdn.asteriskscala.agi.response.{AgiResponse, FailResponse}
import com.github.bespalovdn.asteriskscala.common.logging.LoggerProvider
import io.netty.channel.{ChannelFuture, ChannelFutureListener, ChannelHandlerContext, SimpleChannelInboundHandler}

import scala.concurrent.{Future, Promise}

private [handler]
abstract class AgiCommandResponseChannelHandler extends SimpleChannelInboundHandler[AgiResponse]
    with AgiCommandSender
    with LoggerProvider
{
    def contextProvider: ChannelHandlerContextProvider

    override def send(command: AgiCommand): Future[AgiResponse] = {
        logger.trace("Sending AGI command: " + command)
        responsePromise = Some(Promise[AgiResponse]())
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
            case fail: FailResponse => promise.failure(fail)
            case _ => promise.success(msg)
        }}
        responsePromise = None
    }

    private var responsePromise: Option[Promise[AgiResponse]] = None
}

private [handler]
trait AgiCommandResponseChannelHandlerProvider
{
    def agiCommandResponseChannelHandler: AgiCommandResponseChannelHandler
}