package com.github.bespalovdn.asteriskscala.agi.channel.logging

import com.github.bespalovdn.asteriskscala.agi.handler.ChannelHandlerContextProvider
import com.github.bespalovdn.asteriskscala.common.logging.LoggerTrait
import io.netty.channel.Channel

trait ChannelLoggerTrait extends LoggerTrait
{
    def provider: ChannelHandlerContextProvider

    override protected def logTag: String = {
        val channel = for (
            context <- Option(provider.context);
            channel <- Option(context.channel)
        ) yield channel
        channel match {
            case Some(chan) => logTag(chan)
            case None => super.logTag
        }
    }

    protected [logging] def logTag(chan: Channel): String = super.logTag + "(" + chan.hashCode().toString + ")"
}
