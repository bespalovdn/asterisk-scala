package com.github.bespalovdn.asteriskscala.common.logging.impl

import com.github.bespalovdn.asteriskscala.common.logging.Logger
import org.apache.log4j.{Level, Logger => L4jLogger, NDC}

private [logging]
class LoggerImpl(clazz: Class[_], tag: String) extends Logger
{
    def isTraceEnabled: Boolean = logger.isTraceEnabled
    def isDebugEnabled: Boolean = logger.isDebugEnabled
    def isInfoEnabled: Boolean = logger.isInfoEnabled

    def getLevel: Level = logger.getLevel
    def setLevel(level: Level): Unit = logger.setLevel(level)

    def trace(msg: => String, cause: => Throwable = null) = log(Level.TRACE, msg, cause)
    def debug(msg: => String, cause: => Throwable = null) = log(Level.DEBUG, msg, cause)
    def info(msg: => String, cause: => Throwable = null) = log(Level.INFO, msg, cause)
    def warn(msg: => String, cause: => Throwable = null) = log(Level.WARN, msg, cause)
    def error(msg: => String, cause: => Throwable = null) = log(Level.ERROR, msg, cause)
    def fatal(msg: => String, cause: => Throwable = null) = log(Level.FATAL, msg, cause)

    private def log(level: Level, msg: => Any, cause: => Throwable = null): Unit ={
        if(logger.isEnabledFor(level)) {
            NDC.push(tag)
            logger.log(level, msg, cause)
            NDC.pop()
            NDC.remove()
        }
    }

    private lazy val logger = L4jLogger.getLogger(clazz)
}
