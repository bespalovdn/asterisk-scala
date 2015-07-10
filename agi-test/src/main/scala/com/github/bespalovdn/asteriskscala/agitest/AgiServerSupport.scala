package com.github.bespalovdn.asteriskscala.agitest

import java.net.InetSocketAddress

import akka.actor.ActorRef
import com.github.bespalovdn.asteriskscala.agi.AgiServerActor
import com.github.bespalovdn.asteriskscala.agi.handler.AgiRequestHandlerFactory
import com.github.bespalovdn.asteriskscala.common.test.TestSupportAkka

trait AgiServerSupport extends TestSupportAkka
{
    var agiServer: ActorRef = null

    def agiAddr: InetSocketAddress
    def agiHandlerFactory: AgiRequestHandlerFactory

    override protected def beforeEach() {
        super.beforeEach()
        agiServer = system.newActor("agi-server"){new AgiServerActor(agiAddr, agiHandlerFactory)}
    }

    override protected def afterEach(): Unit = {
        system.stop(agiServer)
        super.afterEach()
    }
}
