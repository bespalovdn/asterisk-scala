package com.github.bespalovdn.asteriskscala.agi.command.response

trait AgiResponse
{
    def resultCode: String
    def resultExtra: String

    override def toString = s"${getClass.getSimpleName}: resultCode=[$resultCode] resultExtra=[$resultExtra]"
}

object AgiResponse
{
    def apply(line: String): AgiResponse = line.take(3) match {
        case "200" => success(line)
        case "510" => FailResponse.InvalidCommand
        case "511" => FailResponse.ChannelIsDead
        case "520" => FailResponse.InvalidSyntax
        case _ if line.startsWith("HANGUP") =>
            // according to http://www.asteriskdocs.org/en/3rd_Edition/asterisk-book-html-chunk/AGI-communication.html
            // if a channel hangs up while your AGI application is still executing, the asterisk will send a line,
            // containing the word HANGUP:
            FailResponse.ChannelIsDead
        case _ => new InvalidAgiResponseException(line) with FailResponse
    }

    private def success(line: String): AgiResponse = new AgiResponse {
        override lazy val resultCode: String = {
            val regex = """200 result=(.+)""".r
            line match {case regex(code) => code}
        }
        override lazy val resultExtra: String = {
            val regex = """200 result=\S+ \((.*)\)""".r
            line match {case regex(a) => a}
        }
    }

    class InvalidAgiResponseException(line: String) extends RuntimeException("Unexpected AGI response: " + line)
}

trait FailResponse extends Throwable with AgiResponse{
    override def resultCode: String = ""
    override def resultExtra: String = ""
}
object FailResponse
{
    case object InvalidCommand extends Exception("Invalid command.") with FailResponse
    case object ChannelIsDead extends Exception("Channel is dead.") with FailResponse
    case object InvalidSyntax extends Exception("Invalid command syntax.") with FailResponse
}

abstract class CustomAgiResponse(source: AgiResponse) extends AgiResponse{
    override def resultCode: String = source.resultCode
    override def resultExtra: String = source.resultExtra
}