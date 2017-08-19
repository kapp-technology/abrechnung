lazy val sharedScalacOptions = Seq(
  "-Xfatal-warnings",
  "-Xfuture",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:_",
  "-unchecked")

lazy val wartremoverOptions = List(
  "Any",
  "AsInstanceOf",
  "DefaultArguments",
  "EitherProjectionPartial",
  "Enumeration",
  "Equals",
  "ExplicitImplicitTypes",
  "FinalCaseClass",
  "FinalVal",
  "ImplicitConversion",
  "IsInstanceOf",
  "JavaConversions",
  "LeakingSealed",
  "MutableDataStructures",
  "NoNeedForMonad",
  "NonUnitStatements",
  "Nothing",
  "Null",
  "Option2Iterable",
  "Overloading",
  "Product",
  "Return",
  "Serializable",
  "StringPlusAny",
  "Throw",
  "ToString",
  "TraversableOps",
  "TryPartial",
  "Var",
  "While").map((s: String) => "-P:wartremover:traverser:org.wartremover.warts." + s)

lazy val nonConsoleOptions =
  wartremoverOptions ++ Seq("-Ywarn-unused-import", -"Xfatal-warnings")

lazy val jvmDependencySettings = Seq.empty

lazy val jsDependencySettings = Seq.empty

//ensime
scalaVersion in ThisBuild := "2.12.3"

lazy val sharedDependencySettings = Seq(
  libraryDependencies ++= Seq(
    compilerPlugin("org.wartremover" %% "wartremover" % "1.2.1"),
    "org.scalatest" %%% "scalatest" % "3.0.1" % "test"))

lazy val sharedSettings =
  sharedDependencySettings ++
  Seq(name := "abrechnung",
      organization := "praxkit",
      scalaVersion := "2.12.3",
      scalacOptions := sharedScalacOptions ++ wartremoverOptions,
      scalacOptions in (Compile, console) ~= (_ filterNot (nonConsoleOptions.contains(_))),
      scalacOptions in (Test, console) := (scalacOptions in (Compile, console)).value)

lazy val abrechnungJVMSettings =
  jvmDependencySettings ++
  Seq(scalacOptions ++= Seq("-Ywarn-dead-code"))

lazy val abrechnungJSSettings =
  jsDependencySettings ++
  Seq(scalacOptions --= Seq("-Ywarn-dead-code"))

lazy val abrechnung = crossProject.in(file("."))
  .settings(sharedSettings: _*)

lazy val abrechnungJVM = abrechnung.jvm
  .settings(abrechnungJVMSettings: _*)

lazy val abrechnungJS = abrechnung.js
  .settings(abrechnungJSSettings: _*)

lazy val root = (project in file("."))
  .enablePlugins(ScalaJSPlugin)
  .aggregate(abrechnungJVM, abrechnungJS)
  .settings(
    publish := {},
    publishLocal := {})
