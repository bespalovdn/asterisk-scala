package com.github.bespalovdn.asteriskscala.agi.handler.impl

import com.github.bespalovdn.asteriskscala.agi.handler.ChannelHandlerContextProvider
import io.netty.channel.ChannelHandlerContext

private [handler]
trait ChannelHandlerContextHolder extends ChannelHandlerContextProvider
{
    var _context: ChannelHandlerContext = null

    override def context: ChannelHandlerContext = _context
}
