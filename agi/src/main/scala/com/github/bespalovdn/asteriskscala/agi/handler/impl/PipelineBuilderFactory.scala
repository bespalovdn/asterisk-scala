package com.github.bespalovdn.asteriskscala.agi.handler.impl

import com.github.bespalovdn.asteriskscala.agi.channel.PipelineBuilder
import com.github.bespalovdn.asteriskscala.agi.transport.{AgiCommandEncoder, AgiCommandResponseDecoder, AgiRequestDecoder}
import io.netty.channel.ChannelPipeline

trait PipelineBuilderFactory
{
    def agiRequestChannelHandler: AgiRequestChannelHandler
    def agiCommandResponseChannelHandler: AgiCommandResponseChannelHandler

    def initialBuilder: PipelineBuilder = new PipelineBuilder {
        override def addDecoders(pipe: ChannelPipeline): Unit = {
            super.addDecoders(pipe)
            pipe.addLast(new AgiRequestDecoder)
        }
        override def addHandlers(pipe: ChannelPipeline): Unit = {
            pipe.addLast(agiRequestChannelHandler)
        }
    }

    def interactionBuilder: PipelineBuilder = new PipelineBuilder {
        override def addDecoders(pipe: ChannelPipeline): Unit = {
            super.addDecoders(pipe)
            pipe.addLast(AgiCommandResponseDecoder)
        }
        override def addEncoders(pipe: ChannelPipeline): Unit = {
            super.addEncoders(pipe)
            pipe.addLast(AgiCommandEncoder)
        }
        override def addHandlers(pipe: ChannelPipeline): Unit = {
            pipe.addLast(agiCommandResponseChannelHandler)
        }
    }
}
