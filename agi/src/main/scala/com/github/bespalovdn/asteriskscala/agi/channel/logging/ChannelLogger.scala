package com.github.bespalovdn.asteriskscala.agi.channel.logging

import com.github.bespalovdn.asteriskscala.agi.handler.ChannelHandlerContextProvider
import com.github.bespalovdn.asteriskscala.common.logging.Logger
import io.netty.channel.Channel

import scala.reflect.ClassTag

object ChannelLogger{
    def apply[T]()(implicit provider: ChannelHandlerContextProvider, classTag: ClassTag[T]): Logger =
        new ChannelLogger[T](provider).logger
}

private class ChannelLogger[T](override val provider: ChannelHandlerContextProvider)
                              (implicit classTag: ClassTag[T])
    extends ChannelLoggerTrait
{
    override protected[logging] def logTag(chan: Channel): String = "(" + chan.hashCode().toString + ")"

    override def defaultLoggerClass: Class[_] = classTag.runtimeClass
}