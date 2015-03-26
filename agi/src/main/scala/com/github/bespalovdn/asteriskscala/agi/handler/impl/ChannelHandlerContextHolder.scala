package com.github.bespalovdn.asteriskscala.agi.handler.impl

import com.github.bespalovdn.asteriskscala.agi.handler.ChannelHandlerContextProvider
import io.netty.channel.ChannelHandlerContext

private [handler]
trait ChannelHandlerContextHolder extends ChannelHandlerContextProvider
{
    override def context: ChannelHandlerContext = _context
    def setContext(context: ChannelHandlerContext): Unit = _context = context

    private var _context: ChannelHandlerContext = null
}
