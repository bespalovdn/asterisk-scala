package com.github.bespalovdn.asteriskscala.agi.command

import com.github.bespalovdn.asteriskscala.agi.command.response.{AgiResponse, ChannelStatusResponse}
import com.github.bespalovdn.asteriskscala.agi.execution.AsyncAction
import com.github.bespalovdn.asteriskscala.agi.handler.AgiHandler
import com.github.bespalovdn.asteriskscala.common.protocol.AsteriskFormatter

import scala.concurrent.Future

/**
 * Returns the status of the specified channel.
 * If no channel name is given the returns the status of the current channel.
 * [[http://www.voip-info.org/wiki/view/channel+status]]
 */
class ChannelStatus private (val channel: Option[String]) extends AgiCommand with AsyncAction
{
    override def toString: String = {
        import AsteriskFormatter._
        "CHANNEL STATUS" + channel.map(" " + _.escaped).getOrElse("")
    }

    override def send()(implicit handler: AgiHandler): Future[ChannelStatusResponse] =
        handler.send(this) map toResult

    private def toResult(response: AgiResponse): ChannelStatusResponse = response.resultCode match {
        case "0" => ChannelStatusResponse.ChannelDownAndAvailable
        case "1" => ChannelStatusResponse.ChannelDownButReserved
        case "2" => ChannelStatusResponse.ChannelIsOffHook
        case "3" => ChannelStatusResponse.DigitsBeenDialed
        case "4" => ChannelStatusResponse.LineIsRinging
        case "5" => ChannelStatusResponse.RemoteIsRinging
        case "6" => ChannelStatusResponse.LineIsUp
        case "7" => ChannelStatusResponse.LineIsBusy
        case other => throw new RuntimeException("Unexpected channel status: " + other)
    }
}

object ChannelStatus
{
    def apply() = new ChannelStatus(None)
    def apply(channel: String) = new ChannelStatus(Some(channel))
}
