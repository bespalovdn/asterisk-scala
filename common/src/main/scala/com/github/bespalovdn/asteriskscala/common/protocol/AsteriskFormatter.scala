package com.github.bespalovdn.asteriskscala.common.protocol

trait AsteriskFormatter
{
    implicit class EscapeAndQuoteString(value: String){
        def escaped: String = escapeAndQuote(value)
    }

    implicit class EscapeAndQuoteTraversable(value: Traversable[String]){
        def escaped: String = escapeAndQuote(value)
    }

    protected def escapeAndQuote(value: String): String = value match {
        case null => "\"\""
        case str => "\"%s\"" format str.
            replaceAll("""\\""", """\\\\""").
            replaceAll("""\"""", """\\"""").
            replaceAll("\\\n", "")
    }

    protected def escapeAndQuote(values: Traversable[String]): String = values match {
        case null => escapeAndQuote(null: String)
        case vs =>
            val str = vs.map{_.replaceAll(",", """\\,""")}.mkString(",")
            escapeAndQuote(str)
    }
}
