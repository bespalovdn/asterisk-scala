package com.github.bespalovdn.asteriskscala.agi.command

import com.github.bespalovdn.asteriskscala.common.protocol.AsteriskFormatter

/**
 * Executes an application with given options.
 * [[http://www.voip-info.org/wiki/view/exec]]
 * @param application Application to be executed.
 * @param options List of application's options.
 */
class Exec private (val application: String, val options: Seq[String]) extends AgiCommand
{
    override def toString: String = {
        import AsteriskFormatter._
        "EXEC %s %s".format(application.escaped, options.escaped)
    }
}

object Exec
{
    def apply(application: String, options: String*) = new Exec(application, options)
}
