package com.github.bespalovdn.asteriskscala.agi.handler

import com.github.bespalovdn.asteriskscala.agi.channel.logging.ChannelLoggerSupport
import com.github.bespalovdn.asteriskscala.agi.command.AgiCommand
import com.github.bespalovdn.asteriskscala.agi.execution.AsyncActionSupport
import com.github.bespalovdn.asteriskscala.agi.handler.impl._
import com.github.bespalovdn.asteriskscala.agi.request.AgiRequest
import com.github.bespalovdn.asteriskscala.agi.response.AgiResponse
import com.github.bespalovdn.asteriskscala.common.logging.Logger
import io.netty.channel.{Channel, ChannelHandlerContext}

import scala.concurrent.Future

trait AgiRequestHandler
    extends ChannelHandlerContextProvider
    with ChannelLoggerSupport
    with AsyncActionSupport
    with AgiCommandSender
{
    self =>

    def handle(request: AgiRequest): Future[Unit]

    override def send(command: AgiCommand): Future[AgiResponse] = impl.agiCommandResponseChannelHandler.send(command)

    def recovery: PartialFunction[Throwable, Future[Unit]] = {
        case err: Throwable => logger.warn("Unhandled error: " + err.getMessage, err).toFuture
    }

    def initializeChannel(channel: Channel) {
        impl.initialBuilder.build(channel.pipeline())
    }

    override def context: ChannelHandlerContext = impl.context

    private object impl extends ChannelHandlerContextHolder
        with AgiRequestChannelHandlerProvider
        with AgiCommandResponseChannelHandlerProvider
        with PipelineBuilderFactory
    {
        override lazy val agiRequestChannelHandler = new AgiRequestChannelHandler {
            override def agiRequestHandlerImpl: AgiRequestHandler = self
            override def contextHolder: ChannelHandlerContextHolder = self.impl
            override def logger: Logger = self.logger
        }

        override lazy val agiCommandResponseChannelHandler = new AgiCommandResponseChannelHandler{
            override def contextProvider: ChannelHandlerContextProvider = self
            override def logger: Logger = self.logger
        }
    }
}
