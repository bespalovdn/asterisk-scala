package com.github.bespalovdn.asteriskscala.agi.command

import com.github.bespalovdn.asteriskscala.common.protocol.AsteriskFormatter

/**
 * Sets AGI variable for the current context.
 * [[http://www.voip-info.org/wiki/view/set+variable]]
 * @param name Name of variable.
 * @param value Value to set.
 */
class SetVariable private (val name: String, val value: String) extends AgiCommand
{
    override def toString: String = {
        import AsteriskFormatter._
        "SET VARIABLE %s %s" format (name.escaped, value.escaped)
    }
}

object SetVariable
{
    def apply(name: String, value: String) = new SetVariable(name, value)
}
