package com.github.bespalovdn.asteriskscala.agi.response

trait AgiResponse

object AgiResponse
{
    def apply(line: String): AgiResponse = line.take(3) match {
        case "200" => success(line)
        case _ => fail(line)
    }

    private def success(line: String): AgiResponseSuccess = ???

    private def fail(line: String): AgiResponseFail = line.take(3) match {
        case "510" => AgiResponseFail.InvalidCommand
        case "511" => AgiResponseFail.ChannelIsDead
        case "520" => AgiResponseFail.InvalidSyntax
        case _ =>
            // according to http://www.asteriskdocs.org/en/3rd_Edition/asterisk-book-html-chunk/AGI-communication.html
            // if a channel hangs up while your AGI application is still executing, the asterisk will send a line,
            // containing the word HANGUP:
            if(line.startsWith("HANGUP"))
                AgiResponseFail.ChannelIsDead
            else
                throw new InvalidAgiResponseException(line)
    }

    class InvalidAgiResponseException(line: String) extends Exception("Unexpected AGI response: " + line)
}

sealed trait AgiResponseFail extends AgiResponse
object AgiResponseFail
{
    case object InvalidCommand extends AgiResponseFail // 510 code
    case object ChannelIsDead extends AgiResponseFail // 511 code
    case object InvalidSyntax extends AgiResponseFail // 520 code
}

trait AgiResponseSuccess extends AgiResponse
