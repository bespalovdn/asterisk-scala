package com.github.bespalovdn.asteriskscala.common.test

import akka.testkit.{ImplicitSender, TestKit, TestKitBase}
import com.github.bespalovdn.asteriskscala.common.akka.ActorFactory

trait TestSupportAkka
    extends TestSupport
    with TestKitBase
    with ImplicitSender
    with ActorFactory
{
    override protected def afterAll() {
        TestKit.shutdownActorSystem(system)
        super.afterAll()
    }
}
