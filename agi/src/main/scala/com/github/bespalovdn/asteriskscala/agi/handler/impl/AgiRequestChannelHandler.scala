package com.github.bespalovdn.asteriskscala.agi.handler.impl

import com.github.bespalovdn.asteriskscala.agi.channel.PipelineBuilder
import com.github.bespalovdn.asteriskscala.agi.execution.AgiAction
import com.github.bespalovdn.asteriskscala.agi.handler.AgiRequestHandler
import com.github.bespalovdn.asteriskscala.agi.request.AgiRequest
import com.github.bespalovdn.scalalog.LoggerSupport
import io.netty.channel.{ChannelHandlerContext, SimpleChannelInboundHandler}

private [handler]
abstract class AgiRequestChannelHandler extends SimpleChannelInboundHandler[AgiRequest]
    with AgiAction
    with LoggerSupport
{
    def contextHolder: ChannelHandlerContextHolder
    def agiRequestHandlerImpl: AgiRequestHandler
    def interactionBuilder: PipelineBuilder

    override def channelActive(ctx: ChannelHandlerContext): Unit = {
        contextHolder.setContext(ctx)
        super.channelActive(ctx)
    }

    override def channelRead0(ctx: ChannelHandlerContext, request: AgiRequest): Unit = {
        logger.info("AGI request received: " + request)
        rebuildPipeline()
        agiRequestHandlerImpl.handle(request).
            recoverWith(agiRequestHandlerImpl.recovery).
            onComplete{case _ => contextHolder.context.close(); logger.info("Done.")}
    }

    private def rebuildPipeline(): Unit ={
        val pipe = contextHolder.context.pipeline()
        while (pipe.last != null)
            pipe.removeLast()
        interactionBuilder.build(pipe)
    }
}

private [handler]
trait AgiRequestChannelHandlerProvider
{
    def agiRequestChannelHandler: AgiRequestChannelHandler
}
