package com.github.bespalovdn.asteriskscala.agi.command

import com.github.bespalovdn.asteriskscala.agi.handler.AgiCommandSender
import com.github.bespalovdn.asteriskscala.agi.response.GetVariableResponse

import scala.concurrent.Future

class GetVariableCommand(variable: String) extends AgiCommand
{
    override def send()(implicit sender: AgiCommandSender): Future[GetVariableResponse] = ???
}
