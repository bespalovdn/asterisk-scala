package com.github.bespalovdn.asteriskscala.agi.transport

import java.util.{List => JList}

import com.github.bespalovdn.asteriskscala.agi.request.AgiRequest
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageDecoder

import scala.collection.mutable.ArrayBuffer

class AgiRequestDecoder extends MessageToMessageDecoder[String]
{
    override def decode(ctx: ChannelHandlerContext, msg: String, out: JList[AnyRef]): Unit = {
        if(msg.isEmpty && buffer.nonEmpty){
            out.add(AgiRequest(buffer))
            buffer.clear()
        }else{
            buffer += msg
        }
    }

    private val buffer = ArrayBuffer.empty[String]
}
