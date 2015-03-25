package com.github.bespalovdn.asteriskscala.agi.channel

import io.netty.channel.ChannelPipeline
import io.netty.handler.codec.LineBasedFrameDecoder
import io.netty.handler.codec.string.{StringDecoder, StringEncoder}

trait PipelineBuilder
{
    import PipelineBuilder._

    def addHandlers(pipe: ChannelPipeline): Unit

    def build(pipe: ChannelPipeline): Unit ={
        pipe.addLast(new LineBasedFrameDecoder(maxLineBasedFrameLength))
        addDecoders(pipe)
        addEncoders(pipe)
        addHandlers(pipe)
    }

    def addDecoders(pipe: ChannelPipeline): Unit ={
        pipe.addLast(stringDecoder)
    }

    def addEncoders(pipe: ChannelPipeline): Unit ={
        pipe.addLast(stringEncoder)
    }
}

object PipelineBuilder
{
    lazy val stringDecoder = new StringDecoder()
    lazy val stringEncoder = new StringEncoder()

    private val maxLineBasedFrameLength = 4 * 1024 // 4 kB
}