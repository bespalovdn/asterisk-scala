package com.github.bespalovdn.asteriskscala.agi.command

import com.github.bespalovdn.asteriskscala.agi.command.response.SuccessResponse
import com.github.bespalovdn.asteriskscala.agi.handler.AgiCommandSender

import scala.concurrent.Future

class Hangup private (val channel: String) extends AgiCommandImpl
{
    override def toString: String = "HANGUP %s" format channel.escaped

    override def send()(implicit sender: AgiCommandSender): Future[SuccessResponse] = sender.send(this)
}

object Hangup extends AgiCommand
{
    override def toString: String = "HANGUP"

    override def send()(implicit sender: AgiCommandSender): Future[SuccessResponse] = sender.send(this)

    def apply(channel: String) = new Hangup(channel)
}
