import sbt.Keys._
import sbt._

object Build extends sbt.Build
{
    lazy val buildSettings: Seq[Setting[_]] = Seq(
        scalaVersion := "2.10.4",
        scalacOptions ++= Seq("-feature", "-unchecked", "-language:postfixOps")
    )

    lazy val agi = project.in(file("agi")).
        settings(name := "agi").
        settings(buildSettings: _*).
        dependsOn(common)

    lazy val agiExamples = project.in(file("agi-examples")).
        settings(name := "agi-examples").
        settings(buildSettings: _*).
        dependsOn(agi, common)

    lazy val common = project.in(file("common")).
        settings(name := "common").
        settings(buildSettings: _*).
        settings(libraryDependencies ++= dependency.akka ++ Seq(
            dependency.log4j,
            dependency.netty,
            dependency.scalaArm,
            dependency.scalaTest,
            dependency.slf4j))

    private object dependency extends Dependencies
}
