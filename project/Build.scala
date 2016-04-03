import sbt.Keys._
import sbt._

object Build extends sbt.Build
{
    lazy val agi = library("agi").
        dependsOn(common)

    lazy val agiExamples = library("agi-examples").
        dependsOn(agi, common)

    lazy val agiTest = library("agi-test").
        dependsOn(agi, common)

    lazy val common = library("common")

    private def library(path: String): Project = Project(path, file(path)).
        settings(libraryDependencies ++= dependency.akka ++ Seq(
            dependency.log4j,
            dependency.netty,
            dependency.scalaArm,
            dependency.scalaTest,
            dependency.slf4j))

    private object dependency extends Dependencies
}
