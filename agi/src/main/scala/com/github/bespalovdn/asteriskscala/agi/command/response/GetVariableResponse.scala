package com.github.bespalovdn.asteriskscala.agi.command.response

sealed trait GetVariableResponse extends CustomAgiResponse

object GetVariableResponse
{
    case class Success(value: String)(val origin: SuccessResponse) extends GetVariableResponse
    case class NotSet()(val origin: SuccessResponse) extends GetVariableResponse
}
