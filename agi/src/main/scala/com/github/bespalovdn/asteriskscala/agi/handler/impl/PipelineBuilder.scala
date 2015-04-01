package com.github.bespalovdn.asteriskscala.agi.handler.impl

import com.github.bespalovdn.asteriskscala.agi.channel.{PipelineBuilder => Builder}
import com.github.bespalovdn.asteriskscala.agi.transport.AgiRequestDecoder
import io.netty.channel.ChannelPipeline

private [handler]
trait PipelineBuilder
{
    this: AgiRequestChannelHandlerFactory =>

    def buildPipeline(pipe: ChannelPipeline): Unit = new Builder {
        override def addDecoders(pipe: ChannelPipeline): Unit = {
            super.addDecoders(pipe)
            pipe.addLast(AgiRequestDecoder.channelHandlerName, new AgiRequestDecoder)
            // NOTE: AgiCommandResponseDecoder will be added later by AgiRequestChannelHandler -
            //       just after AgiRequest received.
        }

        override def addEncoders(pipe: ChannelPipeline): Unit = {
            super.addEncoders(pipe)
            ??? //TODO: add encoders here
        }

        override def addHandlers(pipe: ChannelPipeline): Unit = pipe.addLast(newAgiRequestChannelHandler())
    }.build(pipe)

}
