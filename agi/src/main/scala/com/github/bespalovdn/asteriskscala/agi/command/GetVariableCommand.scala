package com.github.bespalovdn.asteriskscala.agi.command

import com.github.bespalovdn.asteriskscala.agi.execution.AsyncActionSupport
import com.github.bespalovdn.asteriskscala.agi.handler.AgiCommandSender
import com.github.bespalovdn.asteriskscala.agi.response.{GetVariableResponse, SuccessResponse}

import scala.concurrent.Future

class GetVariableCommand(variable: String) extends AgiCommand with AsyncActionSupport
{
    override def toString: String = "GET VARIABLE " + variable

    override def send()(implicit sender: AgiCommandSender): Future[GetVariableResponse] =
        sender.send(this) >>= toResult

    private def toResult(response: SuccessResponse): Future[GetVariableResponse] =
        if(response.resultCode == "0") GetVariableResponse.NotSet.toFuture
        else GetVariableResponse.Success(response.extra).toFuture
}
