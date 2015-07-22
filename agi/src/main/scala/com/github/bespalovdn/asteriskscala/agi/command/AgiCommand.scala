package com.github.bespalovdn.asteriskscala.agi.command

import com.github.bespalovdn.asteriskscala.agi.command.response.SuccessResponse
import com.github.bespalovdn.asteriskscala.agi.handler.AgiCommandSender
import com.github.bespalovdn.asteriskscala.common.protocol.AsteriskFormatter

import scala.concurrent.Future

trait AgiCommand
{
    def send()(implicit sender: AgiCommandSender): Future[SuccessResponse]
}

private [command] trait AgiCommandImpl extends AgiCommand with AsteriskFormatter
{
    override def send()(implicit sender: AgiCommandSender): Future[SuccessResponse] = sender.send(this)
}
