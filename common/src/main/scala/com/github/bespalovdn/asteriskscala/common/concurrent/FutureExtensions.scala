package com.github.bespalovdn.asteriskscala.common.concurrent

import io.netty.util.concurrent
import io.netty.util.concurrent.FutureListener

import scala.concurrent.{ExecutionContext, Future, Promise}

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

trait FutureConversions
{
    type NettyFuture[A] = io.netty.util.concurrent.Future[A]
    type ScalaFuture[A] = scala.concurrent.Future[A]

    // Converts NettyFuture to the ScalaFuture:
    implicit class NettyFutureLike[A](f: NettyFuture[A]) {
        def asScala: Future[A] = {
            val promise = Promise[A]()
            f.addListener(new FutureListener[A] {
                override def operationComplete(future: concurrent.Future[A]): Unit = {
                    if (future.isSuccess)
                        promise.success(future.get)
                    else
                        promise.failure(future.cause)
                }
            })
            promise.future
        }
    }
}