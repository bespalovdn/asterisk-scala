package com.github.bespalovdn.asteriskscala.agi.command

import com.github.bespalovdn.asteriskscala.agi.handler.AgiCommandSender
import com.github.bespalovdn.asteriskscala.agi.response.SuccessResponse

import scala.concurrent.Future

class SetVariableCommand private (name: String, value: String) extends AgiCommand
{
    override def toString: String = """SET VARIABLE %s "%s"""".format(name, value)

    override def send()(implicit sender: AgiCommandSender): Future[SuccessResponse] = sender.send(this)
}

object SetVariableCommand
{
    def apply(name: String, value: String) = new SetVariableCommand(name, value)
}