package com.github.bespalovdn.asteriskscala.agi.response.impl

import com.github.bespalovdn.asteriskscala.agi.response.AgiResponse

private [response]
trait CustomAgiResponse extends AgiResponse
{
    override def response: String = toString
}
