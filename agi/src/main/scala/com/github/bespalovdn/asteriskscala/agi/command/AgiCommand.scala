package com.github.bespalovdn.asteriskscala.agi.command

import com.github.bespalovdn.asteriskscala.agi.command.response.AgiResponse
import com.github.bespalovdn.asteriskscala.agi.handler.AgiHandler

import scala.concurrent.Future

trait AgiCommand
{
    def send()(implicit handler: AgiHandler): Future[AgiResponse] = handler send this
}
