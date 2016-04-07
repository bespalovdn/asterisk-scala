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
        case "0" => new ChannelStatusResponse.ChannelDownAndAvailable(response)
        case "1" => new ChannelStatusResponse.ChannelDownButReserved(response)
        case "2" => new ChannelStatusResponse.ChannelIsOffHook(response)
        case "3" => new ChannelStatusResponse.DigitsBeenDialed(response)
        case "4" => new ChannelStatusResponse.LineIsRinging(response)
        case "5" => new ChannelStatusResponse.RemoteIsRinging(response)
        case "6" => new ChannelStatusResponse.LineIsUp(response)
        case "7" => new ChannelStatusResponse.LineIsBusy(response)
        case other => throw new RuntimeException("Unexpected channel status: " + other)
    }
}

object ChannelStatus
{
    def apply() = new ChannelStatus(None)
    def apply(channel: String) = new ChannelStatus(Some(channel))
}
