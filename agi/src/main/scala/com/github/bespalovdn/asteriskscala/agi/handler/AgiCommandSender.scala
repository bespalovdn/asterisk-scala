package com.github.bespalovdn.asteriskscala.agi.handler

import com.github.bespalovdn.asteriskscala.agi.command.AgiCommand
import com.github.bespalovdn.asteriskscala.agi.response.SuccessResponse

import scala.concurrent.Future

trait AgiCommandSender
{
    def send(command: AgiCommand): Future[SuccessResponse]
}
