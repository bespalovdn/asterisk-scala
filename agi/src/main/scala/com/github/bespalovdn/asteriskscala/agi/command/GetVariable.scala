package com.github.bespalovdn.asteriskscala.agi.command

import com.github.bespalovdn.asteriskscala.agi.command.response.{AgiResponse, CustomAgiResponse}
import com.github.bespalovdn.asteriskscala.agi.execution.AsyncAction
import com.github.bespalovdn.asteriskscala.agi.handler.AgiHandler
import com.github.bespalovdn.asteriskscala.common.protocol.AsteriskFormatter

import scala.concurrent.Future

/**
 * Retrieves AGI variable from current context.
 * [[http://www.voip-info.org/wiki/view/get+variable]]
 *
 * @param variable Name of the variable.
 */
class GetVariable private (val variable: String) extends AgiCommand with AsyncAction
{
    override def toString: String = {
        import AsteriskFormatter._
        "GET VARIABLE " + variable.escaped
    }

    override def send()(implicit handler: AgiHandler): Future[GetVariable.Response] =
        handler.send(this).map{
            case response => response.resultCode match {
                case "0" => new GetVariable.Response.NotSet(response)
                case "1" => new GetVariable.Response.Success(response.resultExtra, response)
            }
        }
}

object GetVariable
{
    def apply(variable: String) = new GetVariable(variable)

    sealed trait Response extends AgiResponse
    object Response{
        class Success(val value: String, source: AgiResponse) extends CustomAgiResponse(source) with Response
        class NotSet(source: AgiResponse) extends CustomAgiResponse(source) with Response
    }
}