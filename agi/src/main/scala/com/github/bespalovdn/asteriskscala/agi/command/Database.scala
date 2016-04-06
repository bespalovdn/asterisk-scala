package com.github.bespalovdn.asteriskscala.agi.command

import com.github.bespalovdn.asteriskscala.agi.command.response.{DatabaseGetResponse, FailResponse, SuccessResponse}
import com.github.bespalovdn.asteriskscala.agi.execution.AsyncAction
import com.github.bespalovdn.asteriskscala.agi.handler.AgiCommandSender

import scala.concurrent.Future

/**
 * Set of database-related commands.
 * [[http://www.voip-info.org/wiki/view/Asterisk+AGI]]
 */
object Database
{
    /**
     * Deletes an entry in the Asterisk database for a given family and key.
     * [[http://www.voip-info.org/wiki/view/database+del]]
     */
    class Del private (val family: String, val key: String) extends AgiCommandImpl {
        override def toString: String = "DATABASE DEL %s %s".format(family.escaped, key.escaped)
    }
    object Del {
        def apply(family: String, key: String) = new Del(family, key)
    }

    /**
     * Deletes a family or specific keytree withing a family in the Asterisk database.
     * [[http://www.voip-info.org/wiki/view/database+deltree]]
     */
    class Deltree private (val family: String, val keytree: Option[String]) extends AgiCommandImpl{
        override def toString = "DATABASE DELTREE " + family.escaped + {keytree match {
            case Some(tree) => " " + tree.escaped
            case None => ""
        }}
    }
    object Deltree{
        def apply(family: String, keytree: String = null) = new Deltree(family, Option(keytree))
    }

    /**
     * Retrieves an entry in the Asterisk database for a given family and key.
     * [[http://www.voip-info.org/wiki/view/database+get]]
     */
    class Get private (val family: String, val key: String) extends AgiCommandImpl with AsyncAction{
        override def toString = "DATABASE GET %s %s".format(family.escaped, key.escaped)

        override def send()(implicit sender: AgiCommandSender): Future[DatabaseGetResponse] =
            sender.send(this) >>= toResult

        private def toResult(origin: SuccessResponse): Future[DatabaseGetResponse] = origin.resultCode match {
            case "0" => DatabaseGetResponse.NotSet(origin).toFuture
            case "1" => DatabaseGetResponse.Some(origin.extra)(origin).toFuture
        }
    }
    object Get{
        def apply(family: String, key: String) = new Get(family, key)
    }

    /**
     * Adds or updates an entry in the Asterisk database for a given family, key, and value.
     * [[http://www.voip-info.org/wiki/view/database+put]]
     */
    class Put private (val family: String, val key: String, val value: String) extends AgiCommandImpl with AsyncAction{
        override def toString = "DATABASE PUT %s %s %s".format(family.escaped, key.escaped, value.escaped)

        override def send()(implicit sender: AgiCommandSender): Future[SuccessResponse] =
            sender.send(this) >>= toResult

        private def toResult(origin: SuccessResponse): Future[SuccessResponse] = origin.resultCode match {
            case "0" => throw FailResponse.Failure("result=0")
            case "1" => origin.toFuture
        }
    }
}
