package com.github.bespalovdn.asteriskscala.agi.channel.logging

import com.github.bespalovdn.asteriskscala.agi.handler.ChannelHandlerContextProvider
import com.github.bespalovdn.asteriskscala.common.logging.LoggerSupport
import io.netty.channel.Channel

trait ChannelLoggerSupport extends ChannelLoggerProvider with LoggerSupport
{
    this: ChannelHandlerContextProvider =>

    override def loggerTag: String = {
        val channel = for (
            _context <- Option(context);
            channel <- Option(_context.channel)
        ) yield channel
        channel match {
            case Some(chan) => loggerTag(chan)
            case None => super.loggerTag
        }
    }

    protected [logging] def loggerTag(chan: Channel): String =
        super.loggerTag + "(" + chan.hashCode().toString + ")"
}
