package com.github.bespalovdn.asteriskscala.agi.command

import com.github.bespalovdn.asteriskscala.agi.handler.AgiCommandSender
import com.github.bespalovdn.asteriskscala.agi.response.AgiResponse

import scala.concurrent.Future

class SetVariableCommand(name: String, value: String) extends AgiCommand
{
    override def toString: String = """SET VARIABLE %s "%s"""".format(name, value)

    override def send()(implicit sender: AgiCommandSender): Future[AgiResponse] = sender.send(this)
}
