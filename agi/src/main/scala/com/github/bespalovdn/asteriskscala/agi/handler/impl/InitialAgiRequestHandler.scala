package com.github.bespalovdn.asteriskscala.agi.handler.impl

import com.github.bespalovdn.asteriskscala.agi.channel.logging.ChannelLoggerTrait
import com.github.bespalovdn.asteriskscala.agi.handler.AgiRequestHandler
import com.github.bespalovdn.asteriskscala.agi.request.AgiRequest
import io.netty.channel.{ChannelHandlerContext, SimpleChannelInboundHandler}

private [handler]
trait InitialAgiRequestHandler extends SimpleChannelInboundHandler[AgiRequest]
{
    this: ChannelHandlerContextHolder with ChannelLoggerTrait =>

    def agiRequestHandlerImpl: AgiRequestHandler

    override def channelActive(ctx: ChannelHandlerContext): Unit = {
        _context = ctx
        super.channelActive(ctx)
    }

    override def channelRead0(ctx: ChannelHandlerContext, request: AgiRequest): Unit = {
        logger.info("Received AGI request: " + request)
        agiRequestHandlerImpl.handle(request).
            recoverWith(agiRequestHandlerImpl.recovery).
            onComplete{case _ => context.close(); logger.info("Done.")}
    }
}

private [handler]
trait InitialAgiRequestHandlerFactory
{
    def createAgiRequestHandler(): InitialAgiRequestHandler
}
