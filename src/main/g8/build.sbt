scalaVersion in ThisBuild := "$scala_version$"

lazy val root = Project(id = "$name$-root", base = file("."))
  .settings(
    name := "$name$-root",
    crossVersion := CrossVersion.binary,
    // Do not publish the root project (it just serves as an aggregate)
    publishArtifact := false,
    publishLocal := {}
  )
  .aggregate(app, macros)

// Define macros in this project.
lazy val macros = project.settings(
  name := "$name$-macros",
  metaMacroSettings,
  // A dependency on scala.meta is required to write new-style macros, but not
  // to expand such macros.  This is similar to how it works for old-style
  // macros and a dependency on scala.reflect.
  libraryDependencies ++= commonDependencies :+ "org.scalameta" %% "scalameta" % "$scalameta_version$"
)

// Use macros in this project.
lazy val app = project
  .settings(name := "$name$-app", metaMacroSettings)
  .dependsOn(macros)

lazy val commonDependencies = Seq(
  "org.scalatest" %% "scalatest" % "$scalatest_version$" % Test
)

lazy val commonSettings = Seq(
  organization := "$organization$",
  version := "$version$",
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding",
    "UTF-8",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-unchecked",
    "-Xfatal-warnings",
    "-Xlint",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Xfuture",
    "-Ywarn-unused-import"
  ),
  wartremoverErrors in (Compile, compile) ++= Warts.unsafe
)

lazy val metaMacroSettings: Seq[Def.Setting[_]] = Seq(
  // A dependency on macro paradise 3.x is required to both write and expand
  // new-style macros.  This is similar to how it works for old-style macro
  // annotations and a dependency on macro paradise 2.x.
  addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M8" cross CrossVersion.full),
  scalacOptions += "-Xplugin-require:macroparadise",
  // temporary workaround for https://github.com/scalameta/paradise/issues/10
  scalacOptions in (Compile, console) := Seq() // macroparadise plugin doesn't work in repl yet.
)
