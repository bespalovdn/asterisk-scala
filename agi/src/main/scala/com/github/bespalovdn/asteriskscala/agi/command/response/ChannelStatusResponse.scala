package com.github.bespalovdn.asteriskscala.agi.command.response

sealed trait ChannelStatusResponse extends CustomResponse
object ChannelStatusResponse
{
    case class ChannelDownAndAvailable(origin: SuccessResponse) extends ChannelStatusResponse
    case class ChannelDownButReserved(origin: SuccessResponse) extends ChannelStatusResponse
    case class ChannelIsOffHook(origin: SuccessResponse) extends ChannelStatusResponse
    case class DigitsBeenDialed(origin: SuccessResponse) extends ChannelStatusResponse
    case class LineIsRinging(origin: SuccessResponse) extends ChannelStatusResponse
    case class RemoteIsRinging(origin: SuccessResponse) extends ChannelStatusResponse
    case class LineIsUp(origin: SuccessResponse) extends ChannelStatusResponse
    case class LineIsBusy(origin: SuccessResponse) extends ChannelStatusResponse
}
