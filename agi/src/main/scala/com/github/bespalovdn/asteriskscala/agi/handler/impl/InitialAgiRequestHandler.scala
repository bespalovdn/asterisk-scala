package com.github.bespalovdn.asteriskscala.agi.handler.impl

import com.github.bespalovdn.asteriskscala.agi.channel.logging.ChannelLoggerSupport
import com.github.bespalovdn.asteriskscala.agi.handler.AgiRequestHandler
import com.github.bespalovdn.asteriskscala.agi.request.AgiRequest
import io.netty.channel.{ChannelHandlerContext, SimpleChannelInboundHandler}

private [handler]
abstract class InitialAgiRequestHandler extends SimpleChannelInboundHandler[AgiRequest]
{
    def contextHolder: ChannelHandlerContextHolder
    def loggerTrait: ChannelLoggerSupport

    def agiRequestHandlerImpl: AgiRequestHandler

    override def channelActive(ctx: ChannelHandlerContext): Unit = {
        contextHolder.setContext(ctx)
        super.channelActive(ctx)
    }

    override def channelRead0(ctx: ChannelHandlerContext, request: AgiRequest): Unit = {
        loggerTrait.logger.info("Received AGI request: " + request)
        agiRequestHandlerImpl.handle(request).
            recoverWith(agiRequestHandlerImpl.recovery).
            onComplete{case _ => contextHolder.context.close(); loggerTrait.logger.info("Done.")}
    }
}

private [handler]
trait InitialAgiRequestHandlerFactory
{
    def createAgiRequestHandler(): InitialAgiRequestHandler
}
