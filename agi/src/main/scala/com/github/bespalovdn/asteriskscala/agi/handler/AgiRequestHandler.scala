package com.github.bespalovdn.asteriskscala.agi.handler

import com.github.bespalovdn.asteriskscala.agi.channel.logging.ChannelLoggerSupport
import com.github.bespalovdn.asteriskscala.agi.execution.AsyncActionSupport
import com.github.bespalovdn.asteriskscala.agi.handler.impl.{AgiRequestChannelHandler, AgiRequestChannelHandlerFactory, ChannelHandlerContextHolder, PipelineBuilder}
import com.github.bespalovdn.asteriskscala.agi.request.AgiRequest
import io.netty.channel.{Channel, ChannelHandlerContext}

import scala.concurrent.Future

trait AgiRequestHandler
    extends ChannelHandlerContextProvider
    with ChannelLoggerSupport
    with AsyncActionSupport
{
    self =>

    def handle(request: AgiRequest): Future[Unit]

    def recovery: PartialFunction[Throwable, Future[Unit]] = {
        case err: Throwable => logger.warn("Unhandled error: " + err.getMessage, err).toFuture
    }

    def initializeChannel(channel: Channel) {
        impl.buildPipeline(channel.pipeline())
    }

    override def context: ChannelHandlerContext = impl.context

    private object impl extends ChannelHandlerContextHolder
        with AgiRequestChannelHandlerFactory
        with PipelineBuilder
    {
        override def newAgiRequestChannelHandler() = new AgiRequestChannelHandler{
            override def agiRequestHandlerImpl: AgiRequestHandler = self
            override def contextHolder: ChannelHandlerContextHolder = self.impl
            override def loggerTrait: ChannelLoggerSupport = self
        }
    }
}
