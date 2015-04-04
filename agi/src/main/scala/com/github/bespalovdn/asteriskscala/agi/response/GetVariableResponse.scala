package com.github.bespalovdn.asteriskscala.agi.response

sealed trait GetVariableResponse extends CustomResponse

object GetVariableResponse
{
    case class Success(value: String)(val origin: SuccessResponse) extends GetVariableResponse
    case class NotSet()(val origin: SuccessResponse) extends GetVariableResponse
}
