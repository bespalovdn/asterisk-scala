package simpleagiserver

import com.github.bespalovdn.asteriskscala.agi.handler.AgiRequestHandler
import com.github.bespalovdn.asteriskscala.agi.request.AgiRequest

import scala.concurrent.Future

class AgiHandler extends AgiRequestHandler
{
    override def handle(request: AgiRequest): Future[Unit] = {

    }
}