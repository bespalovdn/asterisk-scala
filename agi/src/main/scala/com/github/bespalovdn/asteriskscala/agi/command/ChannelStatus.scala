package com.github.bespalovdn.asteriskscala.agi.command

import com.github.bespalovdn.asteriskscala.agi.command.response.ChannelStatusResponse
import com.github.bespalovdn.asteriskscala.agi.execution.AsyncAction
import com.github.bespalovdn.asteriskscala.agi.handler.AgiHandler

import scala.concurrent.Future

/**
 * Returns the status of the specified channel.
 * If no channel name is given the returns the status of the current channel.
 * [[http://www.voip-info.org/wiki/view/channel+status]]
 */
class ChannelStatus private (val channel: Option[String]) extends AgiCommandImpl with AsyncAction
{
    override def toString: String = "CHANNEL STATUS" + {channel match {
        case Some(chan) => " " + chan.escaped
        case None => ""
    }}

    override def send()(implicit handler: AgiHandler): Future[ChannelStatusResponse] =
        sender.send(this) map toResult

    private def toResult(origin: SuccessResponse): ChannelStatusResponse = origin.resultCode match {
        case "0" => ChannelStatusResponse.ChannelDownAndAvailable(origin)
        case "1" => ChannelStatusResponse.ChannelDownButReserved(origin)
        case "2" => ChannelStatusResponse.ChannelIsOffHook(origin)
        case "3" => ChannelStatusResponse.DigitsBeenDialed(origin)
        case "4" => ChannelStatusResponse.LineIsRinging(origin)
        case "5" => ChannelStatusResponse.RemoteIsRinging(origin)
        case "6" => ChannelStatusResponse.LineIsUp(origin)
        case "7" => ChannelStatusResponse.LineIsBusy(origin)
        case other => throw new RuntimeException("Unexpected channel status: " + other)
    }
}

object ChannelStatus
{
    def apply() = new ChannelStatus(None)
    def apply(channel: String) = new ChannelStatus(Some(channel))
}
