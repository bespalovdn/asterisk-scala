package com.github.bespalovdn.asteriskscala.common.protocol

trait AsteriskFormatter
{
    def escapeAndQuote(value: String): String = value match {
        case null => "\"\""
        case str => "\"%s\"" format str.
            replaceAll("""\\""", """\\\\""").
            replaceAll("""\"""", """\\"""").
            replaceAll("\\\n", "")
    }

    def escapeAndQuote(values: Traversable[String]): String = values match {
        case null => escapeAndQuote(null: String)
        case vs =>
            val str = vs.map{_.replaceAll(",", """\\,""")}.mkString(",")
            escapeAndQuote(str)
    }

    implicit class EscapeAndQuote4String(value: String){
        def escaped: String = escapeAndQuote(value)
    }

    implicit class EscapeAndQuote4Traversable(value: Traversable[String]){
        def escaped: String = escapeAndQuote(value)
    }
}
