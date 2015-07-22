package com.github.bespalovdn.asteriskscala.agi.command

/**
 * Executes Asterisk's Playback command.
 * [[http://www.voip-info.org/wiki/view/Asterisk+cmd+Playback]]
 */
object Playback
{
    sealed trait Option
    object Option
    {
        case object Skip extends Option {override def toString = "skip"}
        case object NoAnswer extends Option {override def toString = "noanswer"}
    }

    def apply(file: String, options: Set[Option] = Set.empty): AgiCommand =
        Exec("Playback", file, options.mkString(","))
}
