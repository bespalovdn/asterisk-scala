package com.github.bespalovdn.asteriskscala.agi.channel.logging

import com.github.bespalovdn.asteriskscala.agi.handler.ChannelHandlerContextProvider
import com.github.bespalovdn.asteriskscala.common.logging.Logger
import io.netty.channel.{Channel, ChannelHandlerContext}

import scala.reflect.ClassTag

object ChannelLogger{
    def apply[T]()(implicit provider: ChannelHandlerContextProvider, classTag: ClassTag[T]): Logger =
        new ChannelLogger[T](provider).logger
}

private class ChannelLogger[T](val provider: ChannelHandlerContextProvider)
                              (implicit classTag: ClassTag[T])
    extends ChannelLoggerSupport
    with ChannelHandlerContextProvider
{
    override def context: ChannelHandlerContext = provider.context

    override protected[logging] def loggerTag(chan: Channel): String = "(" + chan.hashCode().toString + ")"

    override def loggerClass: Class[_] = classTag.runtimeClass
}