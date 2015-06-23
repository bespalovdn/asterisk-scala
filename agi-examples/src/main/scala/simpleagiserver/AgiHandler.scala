package simpleagiserver

import com.github.bespalovdn.asteriskscala.agi.command.{Hangup, Playback}
import com.github.bespalovdn.asteriskscala.agi.handler.{AgiRequestHandler, AgiRequestHandlerFactory}
import com.github.bespalovdn.asteriskscala.agi.request.AgiRequest

import scala.concurrent.Future

class AgiHandler extends AgiRequestHandler
{
    override def handle(request: AgiRequest): Future[Unit] = {
        Playback("demo-congrats").send() >> Hangup.send() >> ().toFuture
    }
}

class AgiHandlerFactory extends AgiRequestHandlerFactory
{
    override def createHandler(): AgiRequestHandler = new AgiHandler()
}