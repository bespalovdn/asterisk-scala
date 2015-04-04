package com.github.bespalovdn.asteriskscala.agi.response

import com.github.bespalovdn.asteriskscala.agi.response.impl.CustomAgiResponse

sealed trait GetVariableResult extends AgiResponse

object GetVariableResult
{
    case class Success(value: String) extends GetVariableResult with CustomAgiResponse
    case object NotSet extends GetVariableResult with CustomAgiResponse
}
