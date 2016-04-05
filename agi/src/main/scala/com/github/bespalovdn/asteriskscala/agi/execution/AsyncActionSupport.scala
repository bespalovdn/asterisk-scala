package com.github.bespalovdn.asteriskscala.agi.execution

import com.github.bespalovdn.asteriskscala.common.concurrent.FutureExtensions

import scala.concurrent.ExecutionContext

trait AsyncActionSupport extends FutureExtensions
{
    implicit lazy val executionContext: ExecutionContext = AsyncExecutorService.context
}
