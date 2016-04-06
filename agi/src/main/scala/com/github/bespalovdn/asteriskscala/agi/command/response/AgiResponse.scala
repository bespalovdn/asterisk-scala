package com.github.bespalovdn.asteriskscala.agi.command.response

trait AgiResponse
{
    def resultCode: String
    def extra: String
}

object AgiResponse
{
    def apply(line: String): AgiResponse = line.take(3) match {
        case "200" => success(line)
        case "510" => throw FailResponse.InvalidCommand
        case "511" => throw FailResponse.ChannelIsDead
        case "520" => throw FailResponse.InvalidSyntax
        case _ if line.startsWith("HANGUP") =>
            // according to http://www.asteriskdocs.org/en/3rd_Edition/asterisk-book-html-chunk/AGI-communication.html
            // if a channel hangs up while your AGI application is still executing, the asterisk will send a line,
            // containing the word HANGUP:
            throw FailResponse.ChannelIsDead
        case _ => throw new InvalidAgiResponseException(line)
    }

    private def success(line: String): AgiResponse = new AgiResponse {
        lazy val resultCode: String = {
            val regex = """200 result=(.+)""".r
            line match {case regex(code) => code}
        }
        lazy val extra: String = {
            val regex = """200 result=\S+ \((.*)\)""".r
            line match {case regex(a) => a}
        }
    }

    class InvalidAgiResponseException(line: String) extends RuntimeException("Unexpected AGI response: " + line)
}

object FailResponse
{
    case object InvalidCommand extends Exception("Invalid command.") // 510 code
    case object ChannelIsDead extends Exception("Channel is dead.") // 511 code
    case object InvalidSyntax extends Exception("Invalid command syntax.") // 520 code
}
