package com.github.bespalovdn.asteriskscala.agi.command.response

sealed trait ChannelStatusResponse extends AgiResponse
object ChannelStatusResponse
{
    case object ChannelDownAndAvailable extends ChannelStatusResponse
    case object ChannelDownButReserved extends ChannelStatusResponse
    case object ChannelIsOffHook extends ChannelStatusResponse
    case object DigitsBeenDialed extends ChannelStatusResponse
    case object LineIsRinging extends ChannelStatusResponse
    case object RemoteIsRinging extends ChannelStatusResponse
    case object LineIsUp extends ChannelStatusResponse
    case object LineIsBusy extends ChannelStatusResponse
}
