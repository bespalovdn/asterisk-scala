package com.github.bespalovdn.asteriskscala.agi.command

import com.github.bespalovdn.asteriskscala.agi.handler.AgiCommandSender
import com.github.bespalovdn.asteriskscala.agi.response.SuccessResponse

import scala.concurrent.Future

object Answer extends AgiCommand
{
    override def toString: String = "ANSWER"

    override def send()(implicit sender: AgiCommandSender): Future[SuccessResponse] = sender.send(this)
}
