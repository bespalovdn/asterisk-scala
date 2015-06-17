package com.github.bespalovdn.asteriskscala.agi.command

import com.github.bespalovdn.asteriskscala.agi.execution.AsyncActionSupport
import com.github.bespalovdn.asteriskscala.agi.handler.AgiCommandSender
import com.github.bespalovdn.asteriskscala.agi.response.{GetVariableResponse, SuccessResponse}

import scala.concurrent.Future

class GetVariable private (val variable: String) extends AgiCommandImpl with AsyncActionSupport
{
    override def toString: String = "GET VARIABLE " + variable.escaped

    override def send()(implicit sender: AgiCommandSender): Future[GetVariableResponse] =
        sender.send(this) >>= toResult

    private def toResult(origin: SuccessResponse): Future[GetVariableResponse] =
        if(origin.resultCode == "0") GetVariableResponse.NotSet()(origin).toFuture
        else GetVariableResponse.Success(origin.extra)(origin).toFuture
}

object GetVariable
{
    def apply(variable: String) = new GetVariable(variable)
}