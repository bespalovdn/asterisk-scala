package com.github.bespalovdn.asteriskscala.agi.request

import com.github.bespalovdn.asteriskscala.agi.request.impl.AgiRequestImpl

import scala.util.parsing.combinator.RegexParsers

trait AgiRequest
{
    /**
     * Returns `direction` part of the url, describing AGI request:
     * agi://host[:port][/direction][,arg1,arg2]
     * @return `direction` part of the AGI request.
     */
    def direction: String

    /**
     * Returns list of arguments passed to this AGI request. E.g., for this query:
     * agi://host[:port][/direction][,arg1,arg2]
     * method `arguments` will return: `Seq(arg1, arg2)`
     * @return List of arguments passed to this AGI request.
     */
    def arguments: Seq[String]

    /**
     * Returns list of arguments passed to this AGI request in form of key-value pairs. E.g., for this query:
     * agi://host[:port]/direction,key1=val1,key2=val2
     * method `argumentsKV` will return: `Map(key1 -> val1, key2 -> val2)`
     * @return List of arguments passed to this AGI request.
     */
    def argumentsKV: Map[String, String]

    /**
     * Returns full URL path of the AGI request, e.g.:
     * agi://host[:port][/direction][?param1=value1&param2=value2]
     * @return Full path of the AGI request.
     */
    def url: String

    /**
     * Returns name of calling Asterisk channel.
     * @return Name of calling Asterisk channel.
     */
    def channel: String

    /**
     * Returns unique identifier of the call.
     * @return Unique identifier of the call.
     */
    def uniqueId: String

    /**
     * Returns originating channel type, e.g.: SIP, ZAP...
     * @return The originating channel type.
     */
    def channelType: String

    /**
     * Returns the language code, set to the current channel. E.g.: "en", "ru"...
     * @return The language code.
     */
    def language: String

    /**
     * Returns caller ID number.
     * @return Caller ID number.
     */
    def callerIdNum: String

    /**
     * Returns caller ID name.
     * @return Caller ID name.
     */
    def callerIdName: String

    /**
     * Returns Dialer Number ID (DNID).
     * @return The Dialer Number ID (DNID).
     */
    def dnid: String

    /**
     * Returns referring DNIS number.
     * @return Referring DNIS number.
     */
    def rdnis: String

    /**
     * Returns the dialplan's context, from which this AGI was called.
     * @return The dialplan's context, from which this AGI was called.
     */
    def dialplanContext: String

    /**
     * Returns the dialplan's extension, from which this AGI was called.
     * @return The dialplan's extension, from which this AGI was called.
     */
    def dialplanExtension: String

    /**
     * Returns the dialplan's priority, from which this AGI was called.
     * @return The dialplan's priority, from which this AGI was called.
     */
    def dialplanPriority: Int

    /**
     * Returns the account code of the origin channel.
     * @return The account code of the origin channel.
     */
    def accountCode: String

    /**
     * Returns version of the calling Asterisk server.
     * @return Version of the calling Asterisk server.
     */
    def asteriskVersion: String

    /**
     * Returns the presentation for the callerid in a ZAP channel.
     * @return Presentation for the callerid in a ZAP channel.
     */
    def callingpres: String

    /**
     * Returns the number which is defined in ANI2 (only for PRI Channels).
     * @return The number which is defined in ANI2.
     */
    def callingani2: String

    /**
     * Returns type of number used in PRI Channels.
     * @return Type of number used in PRI Channels.
     */
    def callington: String

    /**
     * Returns an optional 4 digit number (Transit Network Selector) used in PRI Channels.
     * @return An optional 4 digit number (Transit Network Selector) used in PRI Channels.
     */
    def callingtns: String

    /**
     * Returns `true` if started as an EAGI script, `false` otherwise.
     * @return `true` if started as an EAGI script, `false` otherwise.
     */
    def isEnhancedAgi: Boolean
}

private [agi]
object AgiRequest
{
    def apply(lines: Seq[String]): AgiRequest = {
        val source = toMap(lines)
        new AgiRequestImpl(source)
    }

    private def toMap(lines: Seq[String]): Map[String, String] =
        lines.map(toKeyValue).filter(_ != null).toMap

    private def toKeyValue(line: String): (String, String) = line match {
        case null => null
        case _ => lineParser.parse(line)
    }

    private [request]
    object lineParser extends RegexParsers
    {
        override def skipWhitespace: Boolean = false

        def parse(line: String): (String, String) = parseAll(lineParser, line) match {
            case Success(a, _) => a
            case NoSuccess(_, _) => null
        }

        private def lineParser: Parser[(String, String)] = "agi_" ~ keyParser ~ ": " ~ valueParser ~ "\n" ^^ {
            case _ ~ k ~ _ ~ v ~ _ => (k, v)
        }

        private def keyParser: Parser[String] = """[^:]+""".r
        private def valueParser: Parser[String] = """[^\n]+""".r
    }
}
