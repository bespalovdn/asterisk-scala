package com.github.bespalovdn.asteriskscala.agi.response

import com.github.bespalovdn.asteriskscala.agi.response.impl.CustomAgiResponse

sealed trait GetVariableResponse extends AgiResponse

object GetVariableResponse
{
    case class Success(value: String) extends GetVariableResponse with CustomAgiResponse
    case object NotSet extends GetVariableResponse with CustomAgiResponse
}
