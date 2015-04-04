package com.github.bespalovdn.asteriskscala.agi.transport

import java.util

import com.github.bespalovdn.asteriskscala.agi.command.AgiCommand
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageEncoder

@Sharable
object AgiCommandEncoder extends MessageToMessageEncoder[AgiCommand]
{
    override def encode(ctx: ChannelHandlerContext, msg: AgiCommand, out: util.List[AnyRef]): Unit = {
        out.add(msg.toString + "\n")
    }
}
