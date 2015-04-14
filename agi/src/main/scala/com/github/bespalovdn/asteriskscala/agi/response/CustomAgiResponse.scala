package com.github.bespalovdn.asteriskscala.agi.response

private [response]
trait CustomAgiResponse extends SuccessResponse
{
    def origin: SuccessResponse
    final override def response: String = origin.response
}
