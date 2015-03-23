import sbt.Keys._
import sbt._

object Build extends sbt.Build 
{
    lazy val buildSettings: Seq[Setting[_]] = Seq(
        scalaVersion := "2.10.4",
        scalacOptions ++= Seq("-feature", "-unchecked", "-language:postfixOps")
    )

    lazy val asteriskScala = project.in(file(".")).
        settings(name := "asterisk-scala").
        settings(buildSettings: _*)

}
