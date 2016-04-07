package com.github.bespalovdn.asteriskscala.agi.command.response

//TODO: doomed  to die
sealed trait DatabaseGetResponse extends AgiResponse
object DatabaseGetResponse
{
    case class Some(value: String)(val origin: SuccessResponse) extends DatabaseGetResponse
    case class NotSet(origin: SuccessResponse) extends DatabaseGetResponse
}
