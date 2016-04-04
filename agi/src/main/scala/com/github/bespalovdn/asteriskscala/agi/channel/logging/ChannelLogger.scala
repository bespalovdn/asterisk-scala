package com.github.bespalovdn.asteriskscala.agi.channel.logging

import com.github.bespalovdn.asteriskscala.agi.handler.ChannelHandlerContextProvider
import com.github.bespalovdn.scalalog.DynamicLogger

trait ChannelLogger extends DynamicLogger
{
    this: ChannelHandlerContextProvider =>

    override def loggerMDC: Map[String, String] = Option(context).map(_.channel()) match {
        case Some(chan) => super.loggerMDC + ("channel" -> chan.hashCode().toString)
        case None => super.loggerMDC
    }
}