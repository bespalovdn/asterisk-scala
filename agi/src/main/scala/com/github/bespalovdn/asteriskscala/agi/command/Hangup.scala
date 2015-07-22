package com.github.bespalovdn.asteriskscala.agi.command

import com.github.bespalovdn.asteriskscala.agi.command.response.SuccessResponse
import com.github.bespalovdn.asteriskscala.agi.handler.AgiCommandSender

import scala.concurrent.Future

/**
 * Hangs up the specified channel.
 * [[http://www.voip-info.org/wiki/view/hangup]]
 * @param channel The channel to hang up.
 */
class Hangup private (val channel: String) extends AgiCommandImpl
{
    override def toString: String = "HANGUP %s" format channel.escaped
}

object Hangup extends AgiCommand
{
    override def toString: String = "HANGUP"

    override def send()(implicit sender: AgiCommandSender): Future[SuccessResponse] = sender.send(this)

    def apply(channel: String) = new Hangup(channel)
}
