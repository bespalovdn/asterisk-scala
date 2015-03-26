package com.github.bespalovdn.asteriskscala.agi.handler

import com.github.bespalovdn.asteriskscala.agi.channel.logging.ChannelLoggerTrait
import com.github.bespalovdn.asteriskscala.agi.handler.impl.{ChannelHandlerContextHolder, InitialAgiRequestHandler, InitialAgiRequestHandlerFactory, PipelineBuilder}
import com.github.bespalovdn.asteriskscala.agi.request.AgiRequest
import com.github.bespalovdn.asteriskscala.common.concurrent.FutureTraits
import io.netty.channel.{Channel, ChannelHandlerContext}

import scala.concurrent.Future

abstract class AgiRequestHandler (channel: Channel)//TODO: maybe better to move this value out of list of input params?
    extends ChannelHandlerContextProvider
    with ChannelLoggerTrait
    with FutureTraits
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
        with InitialAgiRequestHandlerFactory
        with PipelineBuilder
    {
        override def createAgiRequestHandler() = new InitialAgiRequestHandler{
            override def agiRequestHandlerImpl: AgiRequestHandler = self
            override def contextHolder: ChannelHandlerContextHolder = self.impl
            override def loggerTrait: ChannelLoggerTrait = self
        }
    }
}
