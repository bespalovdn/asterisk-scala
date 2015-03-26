package com.github.bespalovdn.asteriskscala.common.logging

import com.github.bespalovdn.asteriskscala.common.logging.impl.LoggerImpl

import scala.reflect.ClassTag

trait LoggerTrait
{
    def logger: Logger = {
        val (id, l) = _logger
        if(id == logTag) l
        else{
            _logger = (logTag, createLogger(defaultLoggerClass, logTag))
            _logger._2
        }
    }

    def loggerType[A](implicit classTag: ClassTag[A]): Logger = createLogger(classTag.runtimeClass, logTag)

    protected def defaultLoggerClass: Class[_] = getClass

    protected def logTag: String = _logTag

    private def createLogger(clazz: Class[_], tag: String): Logger = new LoggerImpl(clazz, tag)

    private var _logger: (String, Logger) = (null, null)
    private lazy val _logTag = hashCode().toString
}
