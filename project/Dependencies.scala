import sbt._

trait Dependencies
{
    lazy val log4j = "log4j" % "log4j" % "1.2.17"
    lazy val netty = "io.netty" % "netty-all" % "4.0.13.Final"
    lazy val scalaArm = "com.jsuereth" % "scala-arm_2.10" % "1.4"
    lazy val scalaTest = "org.scalatest" % "scalatest_2.10" % "2.2.1" % Test
    lazy val slf4j = "org.slf4j" % "slf4j-log4j12" % "1.7.7"
}