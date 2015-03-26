package com.github.bespalovdn.asteriskscala.common.concurrent

import scala.concurrent.{ExecutionContext, Future, Promise}

trait FutureExtensions
{
    implicit class FutureBuilder[A](value: A)
    {
        def toFuture: Future[A] = Promise.successful(value).future
    }

    implicit class FutureOps[A](f: Future[A])(implicit executorContext: ExecutionContext)
    {
        def >>= [B](handler: A => Future[B]): Future[B] = f flatMap handler
        def >> [B](handler: => Future[B]): Future[B] = f flatMap {_ => handler}
    }
}
