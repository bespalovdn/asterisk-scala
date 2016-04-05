package com.github.bespalovdn.asteriskscala.agi.future

import io.netty.util.concurrent.{Future => NettyFuture, FutureListener => NettyFutureListener}

import scala.concurrent.{Future, Promise}

trait FutureConversions
{
    // Converts NettyFuture to the ScalaFuture:
    implicit class NettyFutureLike[A](f: NettyFuture[A]) {
        def asScala: Future[A] = {
            val promise = Promise[A]()
            f.addListener(new NettyFutureListener[A] {
                override def operationComplete(future: NettyFuture[A]): Unit = {
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
