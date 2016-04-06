package com.github.bespalovdn.asteriskscala.agi.command

import com.github.bespalovdn.asteriskscala.agi.command.response.GetVariableResponse
import com.github.bespalovdn.asteriskscala.agi.execution.AsyncAction
import com.github.bespalovdn.asteriskscala.agi.handler.AgiHandler

import scala.concurrent.Future

/**
 * Retrieves AGI variable from current context.
 * [[http://www.voip-info.org/wiki/view/get+variable]]
 *
 * @param variable Name of the variable.
 */
class GetVariable private (val variable: String) extends AgiCommand with AsyncAction
{
    override def toString: String = "GET VARIABLE " + variable.escaped

    override def send()(implicit handler: AgiHandler): Future[GetVariableResponse] =
        handler.send(this) >>= toResult

    private def toResult(origin: SuccessResponse): Future[GetVariableResponse] =
        if(origin.resultCode == "0") GetVariableResponse.NotSet(origin).toFuture
        else GetVariableResponse.Success(origin.extra)(origin).toFuture
}

object GetVariable
{
    def apply(variable: String) = new GetVariable(variable)
}