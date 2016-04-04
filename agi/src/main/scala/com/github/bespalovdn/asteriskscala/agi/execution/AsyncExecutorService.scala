package com.github.bespalovdn.asteriskscala.agi.execution

import java.util.concurrent._

import com.github.bespalovdn.scalalog.StaticLogger

import scala.concurrent.ExecutionContext

object AsyncExecutorService extends StaticLogger
{
    lazy val context: ExecutionContext = new ExecutionContext {
        override def reportFailure(t: Throwable): Unit = AsyncExecutorService.reporter(t)
        override def execute(runnable: Runnable): Unit = AsyncExecutorService.service.execute(runnable)
    }

    lazy val service: ExecutorService =
    {
        def getInt(name: String, f: String => Int): Int =
            try f(System.getProperty(name)) catch { case e: Exception => Runtime.getRuntime.availableProcessors }
        def range(floor: Int, desired: Int, ceiling: Int): Int =
            if (ceiling < floor) range(ceiling, desired, floor) else scala.math.min(scala.math.max(desired, floor), ceiling)

        val desiredParallelism = range(
            getInt("scala.concurrent.context.minThreads", _.toInt),
            getInt("scala.concurrent.context.numThreads", {
                case null | "" => Runtime.getRuntime.availableProcessors
                case s if s.charAt(0) == 'x' => (Runtime.getRuntime.availableProcessors * s.substring(1).toDouble).ceil.toInt
                case other => other.toInt
            }),
            getInt("scala.concurrent.context.maxThreads", _.toInt))

        val executor = new ThreadPoolExecutor(
            desiredParallelism,
            desiredParallelism,
            5L, TimeUnit.MINUTES,
            new LinkedBlockingQueue[Runnable],
            threadFactory
        )
        executor.allowCoreThreadTimeOut(true)
        executor
    }

    lazy val threadFactory: ThreadFactory = new DefaultThreadFactory(daemonic = true)

    def reporter(cause: Throwable): Unit =
        logger.warn("Unhandled exception detected: " + cause.getMessage, cause)

    private class DefaultThreadFactory(daemonic: Boolean) extends ThreadFactory
    {
        def wire[T <: Thread](thread: T): T = {
            thread.setName(threadName())
            thread.setDaemon(daemonic)
            thread.setUncaughtExceptionHandler(uncaughtExceptionHandler)
            thread
        }

        var threadId = 0
        def threadName(): String = synchronized{ "agi-executor-service-" + {threadId += 1; threadId} }

        def newThread(runnable: Runnable): Thread = wire(new Thread(runnable))
    }

    private val uncaughtExceptionHandler: Thread.UncaughtExceptionHandler = new Thread.UncaughtExceptionHandler {
        def uncaughtException(thread: Thread, cause: Throwable): Unit = reporter(cause)
    }
}
