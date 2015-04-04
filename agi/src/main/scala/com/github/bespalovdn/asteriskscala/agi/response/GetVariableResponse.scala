package com.github.bespalovdn.asteriskscala.agi.response

import com.github.bespalovdn.asteriskscala.agi.response.impl.CustomAgiResponse

case class GetVariableResponse(value: String) extends CustomAgiResponse
