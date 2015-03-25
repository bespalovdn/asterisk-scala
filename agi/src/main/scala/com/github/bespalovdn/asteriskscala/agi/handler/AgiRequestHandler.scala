package com.github.bespalovdn.asteriskscala.agi.handler

import com.github.bespalovdn.asteriskscala.agi.request.AgiRequest
import io.netty.channel.Channel

import scala.concurrent.Future

abstract class AgiRequestHandler (channel: Channel)
{
    def handle(request: AgiRequest): Future[Unit]


}
