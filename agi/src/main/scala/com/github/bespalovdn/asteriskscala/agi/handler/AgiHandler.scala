package com.github.bespalovdn.asteriskscala.agi.handler

import com.github.bespalovdn.asteriskscala.agi.command.AgiCommand
import com.github.bespalovdn.asteriskscala.agi.command.response.AgiResponse
import com.github.bespalovdn.asteriskscala.agi.execution.AsyncAction

import scala.concurrent.Future

trait AgiHandler extends AsyncAction
{
    def send(command: AgiCommand): Future[AgiResponse]

    implicit class AgiCommandsOps(commands: Traversable[AgiCommand])(implicit handler: AgiHandler) {
        def send(): Future[AgiResponse] = {
            val head = commands.head.send()
            commands.tail.foldLeft(head){
                case (response: Future[AgiResponse], command: AgiCommand) => response >> command.send()
            }
        }

    }
}