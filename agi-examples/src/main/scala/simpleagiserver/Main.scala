package simpleagiserver

import java.net.InetSocketAddress

import com.github.bespalovdn.asteriskscala.agi.AgiServer

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Main
{
    val agiBindAddr = InetSocketAddress.createUnresolved("0.0.0.0", 5000)

    def main(args: Array[String]): Unit ={
        val server = new AgiServer(agiBindAddr, new AgiHandlerFactory)
        val lifetime = server.run()
        Await.ready(lifetime.stopped, Duration.Inf)
    }
}
