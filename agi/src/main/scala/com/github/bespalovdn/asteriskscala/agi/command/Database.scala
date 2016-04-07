package com.github.bespalovdn.asteriskscala.agi.command

import com.github.bespalovdn.asteriskscala.agi.command.response.{AgiResponse, DatabaseGetResponse}
import com.github.bespalovdn.asteriskscala.agi.execution.AsyncAction
import com.github.bespalovdn.asteriskscala.agi.handler.AgiHandler
import com.github.bespalovdn.asteriskscala.common.protocol.AsteriskFormatter

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
    class Del private (val family: String, val key: String) extends AgiCommand {
        override def toString: String = {
            import AsteriskFormatter._
            "DATABASE DEL %s %s".format(family.escaped, key.escaped)
        }
    }

    object Del {
        def apply(family: String, key: String) = new Del(family, key)
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Deletes a family or specific keytree withing a family in the Asterisk database.
     * [[http://www.voip-info.org/wiki/view/database+deltree]]
     */
    class Deltree private (val family: String, val keytree: Option[String]) extends AgiCommand{
        override def toString = {
            import AsteriskFormatter._
            "DATABASE DELTREE " + family.escaped + keytree.map(" " + _.escaped).getOrElse("")
        }
    }

    object Deltree{
        def apply(family: String, keytree: String = null) = new Deltree(family, Option(keytree))
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Retrieves an entry in the Asterisk database for a given family and key.
     * [[http://www.voip-info.org/wiki/view/database+get]]
     */
    class Get private (val family: String, val key: String) extends AgiCommand with AsyncAction{
        override def toString = {
            import AsteriskFormatter._
            "DATABASE GET %s %s" format (family.escaped, key.escaped)
        }

        override def send()(implicit handler: AgiHandler): Future[Get.Response] = for{
            response: AgiResponse <- handler send this
            result <- response.resultCode match {
                case "0" => None
                case "1" => Some(response.resultExtra)
            }
        } yield ???

        private def convert(response: AgiResponse): AgiResponse with Option[String] = response.resultCode match {
            case "0" => None with AgiResponse
            case "1" => DatabaseGetResponse.Some(response.resultExtra)(response)
        }
    }

    object Get{
        def apply(family: String, key: String) = new Get(family, key)

        sealed trait Response extends AgiResponse
        object Response{
            case class Success(value: String) extends Response
            case object Fail extends Response
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Adds or updates an entry in the Asterisk database for a given family, key, and value.
     * [[http://www.voip-info.org/wiki/view/database+put]]
     */
    class Put private (val family: String, val key: String, val value: String) extends AgiCommand with AsyncAction{
        override def toString = {
            import AsteriskFormatter._
            "DATABASE PUT %s %s %s".format(family.escaped, key.escaped, value.escaped)
        }

        override def send()(implicit handler: AgiHandler): Future[AgiResponse] =
            handler.send(this) >>= checkResult

        private def checkResult(response: AgiResponse): Future[AgiResponse] = response.resultCode match {
            case "0" => throw new DatabaseUpdateException(response.toString)//TODO: maybe Option[String]?
            case "1" => response.toFuture
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    class DatabaseUpdateException(cause: String) extends RuntimeException(cause)
}
