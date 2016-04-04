package com.github.bespalovdn.asteriskscala.agi.handler

import io.netty.channel.ChannelHandlerContext

trait ChannelHandlerContextProvider
{
    implicit def context: ChannelHandlerContext
}
