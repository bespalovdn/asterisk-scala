package com.github.bespalovdn.asteriskscala.common.test

import com.github.bespalovdn.asteriskscala.common.logging.LoggerSupport
import org.apache.log4j._
import org.scalatest._

trait TestSupport
    extends FlatSpecLike
    with Assertions
    with Matchers
    with BeforeAndAfterAll
    with BeforeAndAfterEach
    with LoggerSupport
    with LoggingInitializer
{
    override protected def beforeAll(): Unit = {
        super.beforeAll()
        initLogging()
    }

    override protected def afterAll(): Unit = {
        shutdownLogging()
        super.afterAll()
    }
}

private [test]
trait LoggingInitializer extends LoggerSupport
{
    def logLevel: Level = Level.DEBUG

    protected def initLogging(): Unit ={
        LogManager.getRootLogger.setLevel(logLevel)
        LogManager.getRootLogger.addAppender(consoleAppender())
        logger.info("Logger initialized with log Level=[%s]".format(LogManager.getRootLogger.getLevel))
    }

    protected def shutdownLogging(): Unit = LogManager.shutdown()

    private def consoleAppender(): Appender ={
        val appender = new ConsoleAppender()
        appender.setTarget("System.out")
        appender.setLayout(new EnhancedPatternLayout(loggingPattern))
        appender.activateOptions()
        appender
    }

    private def loggingPattern: String = """%d [%t] [%x] %-5p [%-10c] %m%n"""
}