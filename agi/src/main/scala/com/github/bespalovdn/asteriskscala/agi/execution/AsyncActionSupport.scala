package com.github.bespalovdn.asteriskscala.agi.execution

import com.github.bespalovdn.asteriskscala.common.concurrent.FutureExtensions

import scala.concurrent.ExecutionContext

//TODO: maybe force them to explicitly specify execution context? remove this trait in this case.
trait AsyncActionSupport extends FutureExtensions
{
    implicit lazy val executionContext: ExecutionContext = AsyncExecutorService.context
}
