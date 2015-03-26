package com.github.bespalovdn.asteriskscala.agi.handler

import com.github.bespalovdn.asteriskscala.agi.channel.logging.ChannelLoggerTrait
import com.github.bespalovdn.asteriskscala.agi.handler.impl.{ChannelHandlerContextHolder, InitialAgiRequestHandler, InitialAgiRequestHandlerFactory, PipelineBuilder}
import com.github.bespalovdn.asteriskscala.agi.request.AgiRequest
import io.netty.channel.{Channel, ChannelHandlerContext}

import scala.concurrent.Future

abstract class AgiRequestHandler (channel: Channel)
    extends ChannelHandlerContextProvider
    with ChannelLoggerTrait
{
    self =>

    def handle(request: AgiRequest): Future[Unit]

    def recovery: PartialFunction[Throwable, Future[Unit]] = {
        case err: Throwable => logger.warn("Unhandled error: " + err.getMessage, err).toFuture
    }

    // build the pipeline:
    impl.buildPipeline(channel.pipeline())

    override def context: ChannelHandlerContext = impl.context

    private object impl extends ChannelHandlerContextHolder
        with ChannelLoggerTrait
        with InitialAgiRequestHandler
        with InitialAgiRequestHandlerFactory
        with PipelineBuilder
    {
        override def provider: ChannelHandlerContextProvider = this
        override def createAgiRequestHandler(): InitialAgiRequestHandler = this
        override def agiRequestHandlerImpl: AgiRequestHandler = self
    }
}
