package com.github.bespalovdn.asteriskscala.agi.execution

import scala.concurrent.{ExecutionContext, Future}
import scala.language.implicitConversions
import scalaz.std.FutureInstances
import scalaz.syntax.ToTypeClassOps

trait AgiAction extends FutureInstances with ToTypeClassOps
{
    implicit def executionContext: ExecutionContext = scala.concurrent.ExecutionContext.global

    implicit def any2unit[A](a: Future[A]): Future[Unit] = a >> ().point[Future]
}
