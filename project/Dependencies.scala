import sbt._

object Dependencies {
  val resolutionRepos = Seq(
    "Typesafe repo" at "http://repo.typesafe.com/typesafe/releases/"
  )

  def compile   (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "compile")
  def provided  (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "provided")
  def test      (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "test")
  def runtime   (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "runtime")
  def container (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "container")

  object V {
    val scala         = "2.10.3"
    val akka          = "2.2.3"
    val scalaz        = "7.0.0"
    val specs2        = "2.0"
    val mockito       = "1.9.5"
    val logback       = "1.0.13"
    val spray         = "1.2.0"
    val sprayJson     = "1.2.5"
    val casbah        = "2.6.2"
  }

  val akka            = "com.typesafe.akka"             %%  "akka-actor"          % V.akka
  val akkaSlf4j       = "com.typesafe.akka"             %%  "akka-slf4j"          % V.akka
  val akkaTest        = "com.typesafe.akka"             %%  "akka-testkit"        % V.akka

  val scalaz          = "org.scalaz"                    %%  "scalaz-core"         % V.scalaz

  val specs2          = "org.specs2"                    %%  "specs2"              % V.specs2
  val mockito         = "org.mockito"                   %   "mockito-core"        % V.mockito

  val logback         = "ch.qos.logback"                %   "logback-classic"     % V.logback

  val sprayCan        = "io.spray"                      %   "spray-can"           % V.spray
  val sprayRouting    = "io.spray"                      %   "spray-routing"       % V.spray
  val sprayJson       = "io.spray"                      %%  "spray-json"          % V.sprayJson

  val casbah          = "org.mongodb"                   %%  "casbah-core"         % V.casbah
}
