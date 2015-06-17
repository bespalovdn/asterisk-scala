package com.github.bespalovdn.asteriskscala.agi.command

object Playback
{
    sealed trait Option
    object Option
    {
        case object Skip extends Option {override def toString = "skip"}
        case object NoAnswer extends Option {override def toString = "noanswer"}
    }

    def apply(file: String, options: Set[Option] = Set.empty) = Exec("Playback", file, options.mkString(","))

    def apply(files: Seq[String], options: Set[Option] = Set.empty) =
        Exec("Playback", files.mkString("&"), options.mkString(","))
}
