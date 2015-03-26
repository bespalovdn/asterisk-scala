package com.github.bespalovdn.asteriskscala.logging

import com.github.bespalovdn.asteriskscala.logging.impl.LoggerImpl

import scala.reflect.ClassTag

trait Logger
{
    def logger: LoggerImpl = {
        val (id, l) = _logger
        if(id == logTag) l
        else{
            _logger = (logTag, createLogger(defaultLoggerClass, logTag))
            _logger._2
        }
    }

    def loggerType[A](implicit classTag: ClassTag[A]): LoggerImpl = createLogger(classTag.runtimeClass, logTag)

    protected def defaultLoggerClass: Class[_] = getClass

    protected def logTag: String = _logTag

    private def createLogger(clazz: Class[_], tag: String): LoggerImpl = new LoggerImpl(clazz, tag)

    private var _logger: (String, LoggerImpl) = (null, null)
    private lazy val _logTag = hashCode().toString
}
