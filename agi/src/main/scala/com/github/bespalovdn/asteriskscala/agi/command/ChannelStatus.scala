package com.github.bespalovdn.asteriskscala.agi.command

import com.github.bespalovdn.asteriskscala.agi.command.response.{ChannelStatusResponse, SuccessResponse}
import com.github.bespalovdn.asteriskscala.agi.execution.AsyncActionSupport
import com.github.bespalovdn.asteriskscala.agi.handler.AgiCommandSender

import scala.concurrent.Future

class ChannelStatus private (val channel: Option[String]) extends AgiCommandImpl with AsyncActionSupport
{
    override def toString: String = "CHANNEL STATUS" + {channel match {
        case Some(chan) => " " + chan.escaped
        case None => ""
    }}

    override def send()(implicit sender: AgiCommandSender): Future[ChannelStatusResponse] =
        sender.send(this) >>= toResult

    private def toResult(origin: SuccessResponse): Future[ChannelStatusResponse] = origin.resultCode match {
        case "0" => ChannelStatusResponse.ChannelDownAndAvailable.toFuture
        case "1" => ChannelStatusResponse.ChannelDownButReserved.toFuture
        case "2" => ChannelStatusResponse.ChannelIsOffHook.toFuture
        case "3" => ChannelStatusResponse.DigitsBeenDialed.toFuture
        case "4" => ChannelStatusResponse.LineIsRinging.toFuture
        case "5" => ChannelStatusResponse.RemoteIsRinging.toFuture
        case "6" => ChannelStatusResponse.LineIsUp.toFuture
        case "7" => ChannelStatusResponse.LineIsBusy.toFuture
        case other => throw new Exception("Unexpected channel status: " + other)
    }
}

object ChannelStatus
{
    def apply() = new ChannelStatus(None)
    def apply(channel: String) = new ChannelStatus(Some(channel))
}
