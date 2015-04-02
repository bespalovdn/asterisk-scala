package com.github.bespalovdn.asteriskscala.common.logging

import org.apache.log4j.Level

trait Logger
{
    def isTraceEnabled: Boolean
    def isDebugEnabled: Boolean
    def isInfoEnabled: Boolean

    def getLevel: Level
    def setLevel(level: Level): Unit

    def trace(msg: => String, cause: => Throwable = null): Unit
    def debug(msg: => String, cause: => Throwable = null): Unit
    def info(msg: => String, cause: => Throwable = null): Unit
    def warn(msg: => String, cause: => Throwable = null): Unit
    def error(msg: => String, cause: => Throwable = null): Unit
    def fatal(msg: => String, cause: => Throwable = null): Unit
}
