package com.github.bespalovdn.asteriskscala.agi.command

import com.github.bespalovdn.asteriskscala.agi.execution.AsyncActionSupport
import com.github.bespalovdn.asteriskscala.agi.handler.AgiCommandSender
import com.github.bespalovdn.asteriskscala.agi.response.{FailResponse, GetVariableResult}

import scala.concurrent.Future

class GetVariableCommand(variable: String) extends AgiCommand with AsyncActionSupport
{
    override def send()(implicit sender: AgiCommandSender): Future[GetVariableResult] = for(
        ar <- sender.send(this);
        result <- ar.response match {
            case "200 result=0" => GetVariableResult.NotSet.toFuture
            case GetVariableCommand.regex(value) => GetVariableResult.Success(value).toFuture
            case other => Future.failed(FailResponse.Error(other))
        }
    ) yield result


}

object GetVariableCommand
{
    private lazy val regex = """200 result=1 \((.*)\)""".r
}