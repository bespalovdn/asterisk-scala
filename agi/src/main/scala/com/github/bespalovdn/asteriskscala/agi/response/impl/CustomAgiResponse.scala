package com.github.bespalovdn.asteriskscala.agi.response.impl

import com.github.bespalovdn.asteriskscala.agi.response.SuccessResponse

private [response]
trait CustomAgiResponse extends SuccessResponse
{
    override def response: String = toString
}
