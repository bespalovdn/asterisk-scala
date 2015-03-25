package com.github.bespalovdn.asteriskscala.agi.handler

import com.github.bespalovdn.asteriskscala.agi.channel.PipelineBuilder
import com.github.bespalovdn.asteriskscala.agi.handler.impl.ChannelHandlerContextHolder
import com.github.bespalovdn.asteriskscala.agi.request.AgiRequest
import io.netty.channel.{Channel, ChannelHandlerContext, ChannelPipeline, SimpleChannelInboundHandler}

import scala.concurrent.Future

abstract class AgiRequestHandler (channel: Channel)
    extends ChannelHandlerContextProvider
{
    def handle(request: AgiRequest): Future[Unit]

    // build the pipeline:
    impl.buildPipeline(channel.pipeline())

    override def context: ChannelHandlerContext = impl.context

    private object impl extends ChannelHandlerContextHolder
        with PipelineBuilderImpl
}

private [handler]
trait PipelineBuilderImpl
{
    def buildPipeline(pipe: ChannelPipeline): Unit = new PipelineBuilder {
        override def addDecoders(pipe: ChannelPipeline): Unit = {
            super.addDecoders(pipe)
            //TODO: add decoders here
        }

        override def addEncoders(pipe: ChannelPipeline): Unit = {
            super.addEncoders(pipe)
            //TODO: add encoders here
        }

        override def addHandlers(pipe: ChannelPipeline): Unit = pipe.addLast(new InitialAgiRequestHandler)
    }.build(pipe)       
}

private [handler]
trait InitialAgiRequestHandler extends SimpleChannelInboundHandler[AgiRequest]
{
    this: ChannelHandlerContextHolder =>
    
    override def channelActive(ctx: ChannelHandlerContext): Unit = {
        _context = ctx
        super.channelActive(ctx)
    }
    
    override def channelRead0(ctx: ChannelHandlerContext, msg: AgiRequest): Unit = ???
}