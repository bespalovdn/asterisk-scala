package com.github.bespalovdn.asteriskscala.common.test

import com.github.bespalovdn.asteriskscala.common.logging.LoggerSupport
import org.apache.log4j.{Appender, ConsoleAppender, EnhancedPatternLayout, Level, Logger => L4jLogger}
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
}

private [test]
trait LoggingInitializer extends LoggerSupport
{
    def logLevel: Level = Level.DEBUG

    protected def initLogging(): Unit ={
        L4jLogger.getRootLogger.setLevel(logLevel)
        L4jLogger.getRootLogger.addAppender(consoleAppender())
        logger.info("Logger initialized with log Level=[%s]".format(L4jLogger.getRootLogger.getLevel))
    }

    private def consoleAppender(): Appender ={
        val appender = new ConsoleAppender()
        appender.setTarget("System.out")
        appender.setLayout(new EnhancedPatternLayout(loggingPattern))
        appender.activateOptions()
        appender
    }

    private def loggingPattern: String = """%d [%t] [%x] %-5p [%-10c] %m%n"""
}