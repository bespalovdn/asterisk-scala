name := "asterisk-scala"

scalaVersion := "2.10.5"

scalacOptions in ThisBuild ++= Seq(
    "-deprecation",
    "-feature",
    "-language:postfixOps",
    "-unchecked"
)

resolvers ++= Seq(
    "sbt-plugin-releases" at "http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/"
)
