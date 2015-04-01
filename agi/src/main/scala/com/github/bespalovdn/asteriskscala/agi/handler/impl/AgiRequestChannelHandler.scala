package com.github.bespalovdn.asteriskscala.agi.handler.impl

import com.github.bespalovdn.asteriskscala.agi.channel.logging.ChannelLoggerSupport
import com.github.bespalovdn.asteriskscala.agi.execution.AsyncActionSupport
import com.github.bespalovdn.asteriskscala.agi.handler.AgiRequestHandler
import com.github.bespalovdn.asteriskscala.agi.request.AgiRequest
import com.github.bespalovdn.asteriskscala.agi.transport.AgiRequestDecoder
import io.netty.channel.{ChannelHandlerContext, SimpleChannelInboundHandler}

private [handler]
abstract class AgiRequestChannelHandler extends SimpleChannelInboundHandler[AgiRequest]
    with AsyncActionSupport
{
    def contextHolder: ChannelHandlerContextHolder
    def loggerTrait: ChannelLoggerSupport

    def agiRequestHandlerImpl: AgiRequestHandler

    override def channelActive(ctx: ChannelHandlerContext): Unit = {
        contextHolder.setContext(ctx)
        super.channelActive(ctx)
    }

    override def channelRead0(ctx: ChannelHandlerContext, request: AgiRequest): Unit = {
        loggerTrait.logger.info("AGI request received: " + request)
        agiRequestHandlerImpl.handle(request).
            recoverWith(agiRequestHandlerImpl.recovery).
            onComplete{case _ => contextHolder.context.close(); loggerTrait.logger.info("Done.")}
        rebuildPipeline()
    }

    private def rebuildPipeline(): Unit ={
        // 1. add AgiCommandResponseDecoder before AgiRequestDecoder
        // 2. remove AgiRequestDecoder
        val pipe = contextHolder.context.pipeline()
        pipe.addBefore(AgiRequestDecoder.channelHandlerName,
            AgiCommandResponseDecoder.channelHandlerName, new AgiCommandResponseDecoder())
        pipe.remove(AgiRequestDecoder.channelHandlerName)

    }
}

private [handler]
trait AgiRequestChannelHandlerFactory
{
    def newAgiRequestChannelHandler(): AgiRequestChannelHandler
}
