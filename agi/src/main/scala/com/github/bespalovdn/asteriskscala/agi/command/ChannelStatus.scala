package com.github.bespalovdn.asteriskscala.agi.command

import com.github.bespalovdn.asteriskscala.agi.command.response.{AgiResponse, CustomAgiResponse}
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

    override def send()(implicit handler: AgiHandler): Future[ChannelStatus.Response] =
        handler.send(this).map{
            case response => response.resultCode match {
                case "0" => new ChannelStatus.Response.ChannelDownAndAvailable(response)
                case "1" => new ChannelStatus.Response.ChannelDownButReserved(response)
                case "2" => new ChannelStatus.Response.ChannelIsOffHook(response)
                case "3" => new ChannelStatus.Response.DigitsBeenDialed(response)
                case "4" => new ChannelStatus.Response.LineIsRinging(response)
                case "5" => new ChannelStatus.Response.RemoteIsRinging(response)
                case "6" => new ChannelStatus.Response.LineIsUp(response)
                case "7" => new ChannelStatus.Response.LineIsBusy(response)
                case other => throw new RuntimeException("Unexpected channel status: " + other)
            }
        }
}

object ChannelStatus
{
    def apply() = new ChannelStatus(None)
    def apply(channel: String) = new ChannelStatus(Some(channel))

    sealed trait Response extends AgiResponse
    object Response {
        class ChannelDownAndAvailable(source: AgiResponse) extends CustomAgiResponse(source) with Response
        class ChannelDownButReserved(source: AgiResponse) extends CustomAgiResponse(source) with Response
        class ChannelIsOffHook(source: AgiResponse) extends CustomAgiResponse(source) with Response
        class DigitsBeenDialed(source: AgiResponse) extends CustomAgiResponse(source) with Response
        class LineIsRinging(source: AgiResponse) extends CustomAgiResponse(source) with Response
        class RemoteIsRinging(source: AgiResponse) extends CustomAgiResponse(source) with Response
        class LineIsUp(source: AgiResponse) extends CustomAgiResponse(source) with Response
        class LineIsBusy(source: AgiResponse) extends CustomAgiResponse(source) with Response
    }
}
