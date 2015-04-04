package com.github.bespalovdn.asteriskscala.agi.command

import com.github.bespalovdn.asteriskscala.agi.execution.AsyncActionSupport
import com.github.bespalovdn.asteriskscala.agi.handler.AgiCommandSender
import com.github.bespalovdn.asteriskscala.agi.response.{AgiResponse, FailResponse, GetVariableResult}

import scala.concurrent.Future

class GetVariableCommand(variable: String) extends AgiCommand with AsyncActionSupport
{
    override def toString: String = "GET VARIABLE " + variable

    override def send()(implicit sender: AgiCommandSender): Future[GetVariableResult] =
        sender.send(this) >>= toResult

    private def toResult(ar: AgiResponse): Future[GetVariableResult] = ar.response match {
        case GetVariableCommand.notSet => GetVariableResult.NotSet.toFuture
        case GetVariableCommand.success(value) => GetVariableResult.Success(value).toFuture
        case other => Future.failed(FailResponse.Error(other))
    }
}

private
object GetVariableCommand
{
    lazy val notSet  = """200 result=0"""
    lazy val success = """200 result=1 \((.*)\)""".r
}