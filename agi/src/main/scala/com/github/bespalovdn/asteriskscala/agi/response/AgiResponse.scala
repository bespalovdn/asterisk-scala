package com.github.bespalovdn.asteriskscala.agi.response

trait AgiResponse

object AgiResponse
{
    def apply(line: String): AgiResponse = line.take(3) match {
        case "200" => success(line)
        case _ => fail(line)
    }

    private def success(line: String): AgiResponse = ???

    private def fail(line: String): FailResponse = line.take(3) match {
        case "510" => FailResponse.InvalidCommand
        case "511" => FailResponse.ChannelIsDead
        case "520" => FailResponse.InvalidSyntax
        case _ =>
            // according to http://www.asteriskdocs.org/en/3rd_Edition/asterisk-book-html-chunk/AGI-communication.html
            // if a channel hangs up while your AGI application is still executing, the asterisk will send a line,
            // containing the word HANGUP:
            if(line.startsWith("HANGUP"))
                FailResponse.ChannelIsDead
            else
                throw new InvalidAgiResponseException(line)
    }

    class InvalidAgiResponseException(line: String) extends Exception("Unexpected AGI response: " + line)
}

sealed trait FailResponse extends Throwable with AgiResponse
object FailResponse
{
    case object InvalidCommand extends Exception("Invalid command.") with FailResponse // 510 code
    case object ChannelIsDead extends Exception("Channel is dead.") with FailResponse // 511 code
    case object InvalidSyntax extends Exception("Invalid command syntax.") with FailResponse // 520 code
}
