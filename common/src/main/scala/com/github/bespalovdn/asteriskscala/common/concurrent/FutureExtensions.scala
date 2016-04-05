package com.github.bespalovdn.asteriskscala.common.concurrent

import scala.concurrent.{ExecutionContext, Future, Promise}


import scalaz._
import scalaz.std.scalaFuture._
import scalaz.Scalaz._
import scalaz.std.FutureInstances

trait FutureExtensions
{
    implicit class FutureBuilder[A](value: A)
    {
        def toFuture: Future[A] = Promise.successful(value).future
    }

    implicit class FutureOps[A](f: Future[A])(implicit context: ExecutionContext)
    {
        def >>= [B](handler: A => Future[B]): Future[B] = f flatMap handler
        def >> [B](handler: => Future[B]): Future[B] = f flatMap {_ => handler}
    }
}

//TODO: complete or remove
trait FutureExt extends FutureInstances
{
    import scala.concurrent.ExecutionContext.Implicits.global

    5.point[Future]
}