package com.github.bespalovdn.asteriskscala.agi.command

import com.github.bespalovdn.asteriskscala.agi.command.response.SuccessResponse
import com.github.bespalovdn.asteriskscala.agi.handler.AgiCommandSender

import scala.concurrent.Future

/**
 * Answer channel if not already in answer state.
 * [[http://www.voip-info.org/wiki/view/answer]]
 */
object Answer extends AgiCommand
{
    override def toString: String = "ANSWER"

    override def send()(implicit sender: AgiCommandSender): Future[SuccessResponse] = sender.send(this)
}
