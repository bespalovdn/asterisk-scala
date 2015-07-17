package com.github.bespalovdn.asteriskscala.agi.command.response

sealed trait GetVariableResponse extends CustomResponse

object GetVariableResponse
{
    case class Success(value: String)(val origin: SuccessResponse) extends GetVariableResponse
    case class NotSet(origin: SuccessResponse) extends GetVariableResponse
}
