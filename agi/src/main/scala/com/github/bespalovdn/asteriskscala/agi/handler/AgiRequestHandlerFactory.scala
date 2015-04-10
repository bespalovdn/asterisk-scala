package com.github.bespalovdn.asteriskscala.agi.handler

trait AgiRequestHandlerFactory
{
    def createHandler(): AgiRequestHandler
}
