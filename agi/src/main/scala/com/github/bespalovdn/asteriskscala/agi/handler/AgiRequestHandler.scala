package com.github.bespalovdn.asteriskscala.agi.handler

import com.github.bespalovdn.asteriskscala.agi.channel.PipelineBuilder
import com.github.bespalovdn.asteriskscala.agi.channel.logging.ChannelLogger
import com.github.bespalovdn.asteriskscala.agi.command.AgiCommand
import com.github.bespalovdn.asteriskscala.agi.command.response.{FailResponse, SuccessResponse}
import com.github.bespalovdn.asteriskscala.agi.execution.AsyncAction
import com.github.bespalovdn.asteriskscala.agi.handler.impl._
import com.github.bespalovdn.asteriskscala.agi.request.AgiRequest
import com.github.bespalovdn.scalalog.LoggerProxy
import io.netty.channel.{Channel, ChannelHandlerContext}

import scala.concurrent.Future

trait AgiRequestHandler
    extends ChannelHandlerContextProvider
    with ChannelLogger
    with AsyncAction
    with AgiCommandSender
{
    selfRef =>

    def handle(request: AgiRequest): Future[Unit]

    override def send(command: AgiCommand): Future[SuccessResponse] = impl.agiCommandResponseChannelHandler.send(command)

    def recovery: PartialFunction[Throwable, Future[Unit]] = {
        case err: FailResponse => logger.info("AgiCommand failed: " + err.getMessage).toFuture
        case err: Throwable => logger.warn("Unhandled error: " + err.getMessage, err).toFuture
    }

    def initializeChannel(channel: Channel) {
        impl.initialBuilder.build(channel.pipeline())
    }

    override def context: ChannelHandlerContext = impl.context

    protected implicit def agiCommandSender: AgiCommandSender = this

    private object impl extends ChannelHandlerContextHolder
        with AgiRequestChannelHandlerProvider
        with AgiCommandResponseChannelHandlerProvider
        with PipelineBuilderFactory
    {
        override lazy val agiRequestChannelHandler = new AgiRequestChannelHandler with LoggerProxy {
            override def loggerSource = selfRef
            override def agiRequestHandlerImpl: AgiRequestHandler = selfRef
            override def contextHolder: ChannelHandlerContextHolder = selfRef.impl
            override def interactionBuilder: PipelineBuilder = impl.interactionBuilder
        }

        override lazy val agiCommandResponseChannelHandler = new AgiCommandResponseChannelHandler with LoggerProxy {
            override def contextProvider: ChannelHandlerContextProvider = selfRef
            override def loggerSource = selfRef
        }
    }
}
