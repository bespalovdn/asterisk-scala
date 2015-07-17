package manual

import java.net.InetSocketAddress

import akka.actor.ActorSystem
import akka.testkit.TestKit
import com.github.bespalovdn.asteriskscala.agi.handler.{AgiRequestHandler, AgiRequestHandlerFactory}
import com.github.bespalovdn.asteriskscala.agi.request.AgiRequest
import com.github.bespalovdn.asteriskscala.agitest.AgiServerSupport
import org.scalatest.DoNotDiscover

import scala.concurrent.Future

@DoNotDiscover
class Test01 extends TestKit(ActorSystem("test"))
    with AgiServerSupport
{
    override def agiAddr: InetSocketAddress = InetSocketAddress.createUnresolved("localhost", 9000)
    override def agiHandlerFactory: AgiRequestHandlerFactory = new AgiRequestHandlerFactory {
        override def createHandler(): AgiRequestHandler = new AgiRequestHandler {
            override def handle(request: AgiRequest): Future[Unit] = {
                logger.info("FAKE HANDLER").toFuture
            }
        }
    }

    "Test01" should
    "run AGI server" in {
        Thread.sleep(1 * 1000)
        logger.info("going down...")
    }
}
