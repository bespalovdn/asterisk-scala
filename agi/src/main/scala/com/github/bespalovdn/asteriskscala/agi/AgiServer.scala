package com.github.bespalovdn.asteriskscala.agi

import java.net.InetSocketAddress

import com.github.bespalovdn.asteriskscala.agi.execution.AsyncActionSupport
import com.github.bespalovdn.asteriskscala.agi.handler.AgiRequestHandlerFactory
import com.github.bespalovdn.asteriskscala.common.concurrent.FutureConversions
import io.netty.channel.Channel

import scala.concurrent.Future

class AgiServer(bindAddr: InetSocketAddress, handlerFactory: AgiRequestHandlerFactory)
    extends AsyncActionSupport
    with FutureConversions
{

    class LifeTime(channel: Future[Channel])
    {
        lazy val started: Future[Unit] = channel >> ().toFuture
        lazy val stopped: Future[Unit] = (channel >>= {ch => ch.closeFuture().asScala}) >> ().toFuture
    }
}
