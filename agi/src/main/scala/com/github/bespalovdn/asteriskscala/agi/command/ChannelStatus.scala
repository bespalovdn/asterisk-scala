package com.github.bespalovdn.asteriskscala.agi.command

import com.github.bespalovdn.asteriskscala.agi.command.response.{ChannelStatusResponse, SuccessResponse}
import com.github.bespalovdn.asteriskscala.agi.execution.AgiAction
import com.github.bespalovdn.asteriskscala.agi.handler.AgiCommandSender

import scala.concurrent.Future

/**
 * Returns the status of the specified channel.
 * If no channel name is given the returns the status of the current channel.
 * [[http://www.voip-info.org/wiki/view/channel+status]]
 */
class ChannelStatus private (val channel: Option[String]) extends AgiCommandImpl with AgiAction
{
    override def toString: String = "CHANNEL STATUS" + {channel match {
        case Some(chan) => " " + chan.escaped
        case None => ""
    }}

    override def send()(implicit sender: AgiCommandSender): Future[ChannelStatusResponse] =
        sender.send(this) >>= toResult

    private def toResult(origin: SuccessResponse): Future[ChannelStatusResponse] = origin.resultCode match {
        case "0" => ChannelStatusResponse.ChannelDownAndAvailable(origin).toFuture
        case "1" => ChannelStatusResponse.ChannelDownButReserved(origin).toFuture
        case "2" => ChannelStatusResponse.ChannelIsOffHook(origin).toFuture
        case "3" => ChannelStatusResponse.DigitsBeenDialed(origin).toFuture
        case "4" => ChannelStatusResponse.LineIsRinging(origin).toFuture
        case "5" => ChannelStatusResponse.RemoteIsRinging(origin).toFuture
        case "6" => ChannelStatusResponse.LineIsUp(origin).toFuture
        case "7" => ChannelStatusResponse.LineIsBusy(origin).toFuture
        case other => throw new Exception("Unexpected channel status: " + other)
    }
}

object ChannelStatus
{
    def apply() = new ChannelStatus(None)
    def apply(channel: String) = new ChannelStatus(Some(channel))
}
