package com.github.bespalovdn.asteriskscala.examples.simpleagiserver

import com.github.bespalovdn.asteriskscala.agi.command.{Answer, Hangup, Playback}
import com.github.bespalovdn.asteriskscala.agi.handler.{AgiRequestHandler, AgiRequestHandlerFactory}
import com.github.bespalovdn.asteriskscala.agi.request.AgiRequest

import scala.concurrent.Future

class AgiHandlerImpl extends AgiRequestHandler
{
    override def handle(request: AgiRequest): Future[Unit] = {
        Seq(Answer, Playback("demo-congrats"), Hangup).send()
    }
}

class AgiHandlerFactory extends AgiRequestHandlerFactory
{
    override def createHandler(): AgiRequestHandler = new AgiHandlerImpl()
}