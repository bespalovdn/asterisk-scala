package com.github.bespalovdn.asteriskscala.agi

import java.net.InetSocketAddress

import akka.actor.Actor
import com.github.bespalovdn.asteriskscala.agi.handler.AgiRequestHandlerFactory
import com.github.bespalovdn.asteriskscala.common.concurrent.FutureExtensions

import scala.concurrent.duration._

class AgiServerActor (bindAddr: InetSocketAddress,
                      handlerFactory: AgiRequestHandlerFactory,
                      recoveryInterval: FiniteDuration = 30 seconds)
    extends Actor
    with LoggerSupport
    with FutureExtensions
{
    var lifetime: AgiServer#LifeTime = null
    var stopped = false

    override def preStart(): Unit = {
        super.preStart()
        startServer()
    }

    override def postStop(): Unit = {
        stopServer()
        super.postStop()
    }

    override def receive: Receive = {
        case InternalMsg.Restart => startServer()
        case InternalMsg.Stopped =>
            if(!stopped){
                logger.warn("AGI server unexpectedly stopped. Will try to recovery in %s..." format recoveryInterval)
                scheduleRestart()
            }
    }

    def startServer(): Unit ={
        try {
            import context.dispatcher
            lifetime = new AgiServer(bindAddr, handlerFactory).run()
            lifetime.stopped >> (self ! InternalMsg.Stopped).toFuture
            stopped = false
        }catch{
            case e: Throwable =>
                val msg = "Unable to start AGI server. Reason: %s. Will try to recovery in %s... ".format(
                    e.getMessage, recoveryInterval)
                logger.warn(msg, e)
                scheduleRestart()
        }
    }

    def stopServer(): Unit ={
        stopped = true
        lifetime.stop()
    }

    def scheduleRestart(): Unit ={
        import context.dispatcher
        context.system.scheduler.scheduleOnce(recoveryInterval, self, InternalMsg.Restart)
    }

    private object InternalMsg
    {
        case object Restart
        case object Stopped
    }
}
