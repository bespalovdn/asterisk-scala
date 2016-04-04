package com.github.bespalovdn.asteriskscala.agitest

import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets

import com.github.bespalovdn.scalalog.StaticLogger
import org.apache.log4j.xml.DOMConfigurator
import org.apache.log4j.{Level, LogManager => Log4jLogManager}
import org.scalatest._

import scala.xml.{Elem, NodeSeq}

trait TestSupport
    extends FlatSpecLike
    with Assertions
    with Matchers
    with BeforeAndAfterAll
    with BeforeAndAfterEach
    with StaticLogger
    with Log4jSettings
{
    override def log4jLevel: Level = Level.WARN

    override protected def beforeAll(): Unit = {
        super.beforeAll()
        LogManager4UT.initialize(this)
    }

    override protected def afterAll(): Unit = {
        LogManager4UT.shutdown()
        super.afterAll()
    }
}

trait Log4jSettings
{
    type LoggerDef = (String, Level)

    def log4jLevel: Level = Level.DEBUG
    def log4jLoggers: Set[LoggerDef] = Set.empty
    def log4jPattern: String = "%d [%X{tag}] %p [%c] %m%n"
    def log4jFilter: Set[LogFilter] = Set.empty
}

case class LogFilter(level: Option[Level], className: Option[String], message: Option[String]){
    def isDefined: Boolean = level.isDefined || className.isDefined || message.isDefined
}

object LogFilter
{
    def apply(level: Level = null, className: String = null, message: String = null): LogFilter =
        LogFilter(Option(level), Option(className), Option(message))
}

object LogManager4UT
{
    def initialize(settings: Log4jSettings): Unit = {
        val xml = log4jxml(settings)
        val printer = XmlTrimPrinter
        val xmlDoc = printer.xmlHeader + """<!DOCTYPE log4j:configuration SYSTEM "log4j.dtd">""" + printer.print(xml)
        val stream = new ByteArrayInputStream(xmlDoc.getBytes(StandardCharsets.UTF_8))
        new DOMConfigurator().doConfigure(stream, Log4jLogManager.getLoggerRepository)
    }

    def shutdown(): Unit = Log4jLogManager.shutdown()

    private def log4jxml(settings: Log4jSettings): Elem = {
        <log4j:configuration reset="true" xmlns:log4j="http://jakarta.apache.org/log4j/">
            <appender name="stdout" class="org.apache.log4j.ConsoleAppender">
                <param name="Target" value="System.out"/>
                <layout class="org.apache.log4j.PatternLayout">
                    <param name="ConversionPattern" value={settings.log4jPattern}/>
                </layout>
            </appender>
            <appender name="ASYNC" class="org.apache.log4j.AsyncAppender">
                <param name="BufferSize" value="1024"/>
                <param name="Blocking" value="false"/>
                {settings.log4jFilter.map(filter2xml)}
                <appender-ref ref="stdout"/>
            </appender>
            {settings.log4jLoggers.map{case (clazz, level) =>
            <logger name={clazz} additivity="false">
                <level value={level.toString}/>
                <appender-ref ref="ASYNC"/>
            </logger>
        }}
            <root>
                <priority value={settings.log4jLevel.toString}/>
                <appender-ref ref="ASYNC"/>
            </root>
        </log4j:configuration>
    }

    private def filter2xml(f: LogFilter): NodeSeq = {
        if (f.isDefined)
            <filter class="lib.loggerimpl.log4j.filters.CustomDropFilter">
                {f.level.map(lvl => <param name="Level" value={lvl.toString}/>).getOrElse(NodeSeq.Empty)}
                {f.className.map(cls => <param name="ClassName" value={cls}/>).getOrElse(NodeSeq.Empty)}
                {f.message.map(msg => <param name="Message" value={msg}/>).getOrElse(NodeSeq.Empty)}
            </filter>
        else
            NodeSeq.Empty
    }
}
