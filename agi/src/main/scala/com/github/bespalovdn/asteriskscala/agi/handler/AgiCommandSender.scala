package com.github.bespalovdn.asteriskscala.agi.handler

import com.github.bespalovdn.asteriskscala.agi.command.AgiCommand
import com.github.bespalovdn.asteriskscala.agi.response.AgiResponse

import scala.concurrent.Future

trait AgiCommandSender
{
    def send(command: AgiCommand): Future[AgiResponse]
}
