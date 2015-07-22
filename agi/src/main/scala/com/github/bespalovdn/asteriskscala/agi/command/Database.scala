package com.github.bespalovdn.asteriskscala.agi.command

/**
 * Set of database-related commands.
 * [[http://www.voip-info.org/wiki/view/Asterisk+AGI]]
 */
object Database
{
    // Deletes an entry in the Asterisk database for a given family and key.
    class Del private (val family: String, val key: String) extends AgiCommandImpl
    {
        override def toString: String = "DATABASE DEL %s %s".format(family.escaped, key.escaped)
    }

    object Del
    {
        def apply(family: String, key: String) = new Del(family, key)
    }
}
