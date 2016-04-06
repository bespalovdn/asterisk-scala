package com.github.bespalovdn.asteriskscala.agi.command.response

sealed trait DatabaseGetResponse extends AgiResponse
object DatabaseGetResponse
{
    case class Some(value: String)(val origin: SuccessResponse) extends DatabaseGetResponse
    case class NotSet(origin: SuccessResponse) extends DatabaseGetResponse
}
