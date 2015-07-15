package com.github.bespalovdn.asteriskscala.agi.transport

import java.util.{List => JList}

import com.github.bespalovdn.asteriskscala.agi.command.response.AgiResponse
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageDecoder

@Sharable
object AgiCommandResponseDecoder extends MessageToMessageDecoder[String]
{
    def channelHandlerName = AgiCommandResponseDecoder.getClass.getSimpleName

    override def decode(ctx: ChannelHandlerContext, msg: String, out: JList[AnyRef]): Unit = out.add(AgiResponse(msg))
}