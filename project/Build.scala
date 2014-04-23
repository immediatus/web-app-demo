import sbt._
import Keys._

object Build extends sbt.Build {
  import Dependencies._

  lazy val commonSettings = Defaults.defaultSettings ++ Seq(
    organization  := "com.immediatus",
    version       := "0.1-PoC",
    scalaVersion  := V.scala,
    scalacOptions := Seq("-deprecation", "-unchecked", "-encoding", "utf8"),
    resolvers     ++= Dependencies.resolutionRepos
  )

  lazy val domain   = Project("domain", file("./domain"), settings = commonSettings)
    .settings {
      libraryDependencies ++=
        compile(akka, casbah) ++
        test(specs2, mockito, akkaTest)
    }

  lazy val web      = Project("web", file("./web"), settings = commonSettings)
    .settings {
      libraryDependencies ++=
        compile(akka, akkaSlf4j, sprayCan, sprayRouting, sprayJson, logback) ++
        test(specs2, mockito, akkaTest)
    }
    .dependsOn(domain)
}
