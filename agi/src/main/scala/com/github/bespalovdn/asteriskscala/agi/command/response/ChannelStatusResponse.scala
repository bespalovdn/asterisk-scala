package com.github.bespalovdn.asteriskscala.agi.command.response

sealed trait ChannelStatusResponse extends CustomAgiResponse
object ChannelStatusResponse
{
    class ChannelDownAndAvailable(source: AgiResponse) extends ChannelStatusResponse
    class ChannelDownButReserved(source: AgiResponse) extends ChannelStatusResponse
    class ChannelIsOffHook(source: AgiResponse) extends ChannelStatusResponse
    class DigitsBeenDialed(source: AgiResponse) extends ChannelStatusResponse
    class LineIsRinging(source: AgiResponse) extends ChannelStatusResponse
    class RemoteIsRinging(source: AgiResponse) extends ChannelStatusResponse
    class LineIsUp(source: AgiResponse) extends ChannelStatusResponse
    class LineIsBusy(source: AgiResponse) extends ChannelStatusResponse
}
