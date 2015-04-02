package com.github.bespalovdn.asteriskscala.agi.response

sealed trait AgiResponseFail extends AgiResponse
object AgiResponseFail
{
    case object InvalidCommand extends AgiResponseFail // 510 code
    case object ChannelIsDead extends AgiResponseFail // 511 code
    case object InvalidSyntax extends AgiResponseFail // 520 code
}
