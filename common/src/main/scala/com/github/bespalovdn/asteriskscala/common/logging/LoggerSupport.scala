package com.github.bespalovdn.asteriskscala.common.logging

import com.github.bespalovdn.asteriskscala.common.logging.impl.LoggerImpl

import scala.reflect.ClassTag

trait LoggerSupport
{
    def logger: Logger = createLogger(loggerClass)

    def loggerType[A](implicit classTag: ClassTag[A]): Logger = createLogger(classTag.runtimeClass)

    def loggerClass: Class[_] = getClass

    def loggerTag: String = hashCode().toString

    private def createLogger(clazz: Class[_]) = new LoggerImpl(clazz, loggerTag)
}
