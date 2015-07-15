package com.github.bespalovdn.asteriskscala.agi.command.response

trait AgiResponse
{
    def response: String
}

object AgiResponse
{
    def apply(line: String): AgiResponse = line.take(3) match {
        case "200" => if(line == "200 result=-1") FailResponse.Failure(line) else success(line)
        case _ => fail(line)
    }

    private def success(line: String): SuccessResponse = new SuccessResponse {
        override def response: String = line
    }

    private def fail(line: String): FailResponse = line.take(3) match {
        case "510" => FailResponse.InvalidCommand(line)
        case "511" => FailResponse.ChannelIsDead(line)
        case "520" => FailResponse.InvalidSyntax(line)
        case _ =>
            // according to http://www.asteriskdocs.org/en/3rd_Edition/asterisk-book-html-chunk/AGI-communication.html
            // if a channel hangs up while your AGI application is still executing, the asterisk will send a line,
            // containing the word HANGUP:
            if(line.startsWith("HANGUP"))
                FailResponse.ChannelIsDead(line)
            else
                throw new InvalidAgiResponseException(line)
    }

    class InvalidAgiResponseException(line: String) extends Exception("Unexpected AGI response: " + line)
}

trait SuccessResponse extends AgiResponse
{
    lazy val resultCode: String = {
        val regex = """200 result=(.+)""".r
        response match {case regex(code) => code}
    }

    lazy val extra: String = {
        val regex = """200 result=\S+ \((.*)\)""".r
        response match {case regex(a) => a}
    }
}

sealed trait FailResponse extends Throwable with AgiResponse
object FailResponse
{
    case class InvalidCommand(response: String) extends Exception("Invalid command.") with FailResponse // 510 code
    case class ChannelIsDead(response: String) extends Exception("Channel is dead.") with FailResponse // 511 code
    case class InvalidSyntax(response: String) extends Exception("Invalid command syntax.") with FailResponse // 520 code
    case class Failure(response: String) extends Exception("Failure: " + response) with FailResponse // other error (e.g. 200 result=-1)
}
