package com.github.bespalovdn.asteriskscala.agi.command

import com.github.bespalovdn.asteriskscala.agi.handler.AgiCommandSender
import com.github.bespalovdn.asteriskscala.agi.response.SuccessResponse

import scala.concurrent.Future

class SetVariable private (val name: String, val value: String) extends AgiCommandImpl
{
    override def toString: String = "SET VARIABLE %s %s".format(name.escaped, value.escaped)

    override def send()(implicit sender: AgiCommandSender): Future[SuccessResponse] = sender.send(this)
}

object SetVariable
{
    def apply(name: String, value: String) = new SetVariable(name, value)
}
