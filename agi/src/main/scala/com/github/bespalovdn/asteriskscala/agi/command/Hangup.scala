package com.github.bespalovdn.asteriskscala.agi.command

import com.github.bespalovdn.asteriskscala.common.protocol.AsteriskFormatter

/**
 * Hangs up the specified channel.
 * [[http://www.voip-info.org/wiki/view/hangup]]
  *
  * @param channel The channel to hang up.
 */
class Hangup private (val channel: String) extends AgiCommand
{
    override def toString: String = {
        import AsteriskFormatter._
        "HANGUP " + channel.escaped
    }
}

object Hangup extends AgiCommand
{
    override def toString: String = "HANGUP"

    def apply(channel: String) = new Hangup(channel)
}
