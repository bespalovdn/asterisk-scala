package com.github.bespalovdn.asteriskscala.examples.simpleagiserver

import java.net.InetSocketAddress

import com.github.bespalovdn.asteriskscala.agi.AgiServer

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Main
{
    /**
     * Address where to listen incoming AGI requests.
     */
    val agiBindAddr = InetSocketAddress.createUnresolved("0.0.0.0", 5000)

    def main(args: Array[String]): Unit ={
        // create the AgiServer instance
        val server = new AgiServer(agiBindAddr, new AgiHandlerFactory)
        // run the server
        val lifetime = server.run()
        // wait until server stopped
        Await.ready(lifetime.stopped, Duration.Inf)
    }
}
