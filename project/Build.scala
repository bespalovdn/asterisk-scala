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
        settings(libraryDependencies ++= dependency.akka ++ dependency.log ++ Seq(
            dependency.netty,
            dependency.scalaArm,
            dependency.scalaTest))

    private object dependency {
        lazy val akka = Seq(
            "com.typesafe.akka" % "akka-actor_2.10" % "2.3.4",
            "com.typesafe.akka" % "akka-slf4j_2.10" % "2.3.4",
            "com.typesafe.akka" % "akka-testkit_2.10" % "2.3.4")
        lazy val netty = "io.netty" % "netty-all" % "4.0.13.Final"
        lazy val scalaArm = "com.jsuereth" % "scala-arm_2.10" % "1.4"
        lazy val scalaTest = "org.scalatest" % "scalatest_2.10" % "2.2.1"
        lazy val log = Seq(
            "org.slf4j" % "slf4j-api" % "1.7.12",
            "com.github.bespalovdn" % "scala-log_2.10" % "1.0-SNAPSHOT",
            "org.slf4j" % "slf4j-log4j12" % "1.7.12" % "test",
            "log4j" % "log4j" % "1.2.17" % "test"
        )
    }
}
