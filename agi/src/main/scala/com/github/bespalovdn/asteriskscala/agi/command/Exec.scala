package com.github.bespalovdn.asteriskscala.agi.command

import com.github.bespalovdn.asteriskscala.agi.handler.AgiCommandSender
import com.github.bespalovdn.asteriskscala.agi.response.SuccessResponse

import scala.concurrent.Future

class Exec private (val application: String, val options: Seq[String]) extends AgiCommandImpl
{
    override def toString: String = "EXEC %s %s".format(application.escaped, options.escaped)

    override def send()(implicit sender: AgiCommandSender): Future[SuccessResponse] = sender.send(this)
}

object Exec
{
    def apply(application: String, options: String*) = new Exec(application, options)
}
