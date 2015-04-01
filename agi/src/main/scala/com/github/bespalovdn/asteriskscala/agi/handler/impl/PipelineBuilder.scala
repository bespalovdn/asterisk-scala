package com.github.bespalovdn.asteriskscala.agi.handler.impl

import com.github.bespalovdn.asteriskscala.agi.channel.{PipelineBuilder => Builder}
import com.github.bespalovdn.asteriskscala.agi.transport.AgiRequestDecoder
import io.netty.channel.ChannelPipeline

private [handler]
trait PipelineBuilder
{
    this: InitialAgiRequestHandlerFactory =>

    def buildPipeline(pipe: ChannelPipeline): Unit = new Builder {
        override def addDecoders(pipe: ChannelPipeline): Unit = {
            super.addDecoders(pipe)
            pipe.addLast(AgiRequestDecoder.channelHandlerName, new AgiRequestDecoder) //TODO: remove it after get the request
            //TODO: add AgiCommandResponseDecoder
        }

        override def addEncoders(pipe: ChannelPipeline): Unit = {
            super.addEncoders(pipe)
            //TODO: add encoders here
        }

        override def addHandlers(pipe: ChannelPipeline): Unit = pipe.addLast(createAgiRequestHandler())
    }.build(pipe)
}
