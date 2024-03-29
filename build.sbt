lazy val CatsEffectVersion = "1.3.1"
lazy val Fs2Version        = "1.0.5"
lazy val Http4sVersion     = "0.20.3"
lazy val CirceVersion      = "0.11.1"
lazy val DoobieVersion     = "0.8.0-M1"
lazy val H2Version         = "1.4.196"
lazy val FlywayVersion     = "5.0.5"
lazy val LogbackVersion    = "1.2.3"
lazy val ScalaTestVersion  = "3.0.8"
lazy val ScalaCheckVersion = "1.14.0"
lazy val PureconfigVersion = "0.11.1"
lazy val assemblyFolder = file("assembly")
lazy val ignoreFiles = List("application.conf.sample")

enablePlugins(DockerPlugin)
resolvers += Resolver.sonatypeRepo("releases")
addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3")

parallelExecution in Test := false
parallelExecution in IntegrationTest := false

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    Defaults.itSettings,
    organization := "co.ledger",
    name := "hackathon-blockchain-explorer",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.8",
    // Inspired by https://tpolecat.github.io/2017/04/25/scalac-flags.html
    addCommandAlias("beforePush", ";scalastyle;compile"),
    scalacOptions ++= Seq(
      "-deprecation",                      // Emit warning and location for usages of deprecated APIs.
      "-encoding", "utf-8",                // Specify character encoding used by source files.
      "-explaintypes",                     // Explain type errors in more detail.
      "-feature",                          // Emit warning and location for usages of features that should be imported explicitly.
      "-language:existentials",            // Existential types (besides wildcard types) can be written and inferred
      "-language:higherKinds",             // Allow higher-kinded types
      "-language:implicitConversions",     // Allow definition of implicit functions called views
      "-unchecked",                        // Enable additional warnings where generated code depends on assumptions.
      "-Xfatal-warnings",                  // Fail the compilation if there are any warnings.
      "-Xlint:constant",                   // Evaluation of a constant arithmetic expression results in an error.
      "-Xlint:delayedinit-select",         // Selecting member of DelayedInit.
      "-Xlint:doc-detached",               // A Scaladoc comment appears to be detached from its element.
      "-Xlint:inaccessible",               // Warn about inaccessible types in method signatures.
      "-Xlint:infer-any",                  // Warn when a type argument is inferred to be `Any`.
      "-Xlint:missing-interpolator",       // A string literal appears to be missing an interpolator id.
      "-Xlint:nullary-override",           // Warn when non-nullary `def f()' overrides nullary `def f'.
      "-Xlint:nullary-unit",               // Warn when nullary methods return Unit.
      "-Xlint:option-implicit",            // Option.apply used implicit view.
      "-Xlint:poly-implicit-overload",     // Parameterized overloaded implicit methods are not visible as view bounds.
      "-Xlint:private-shadow",             // A private field (or class parameter) shadows a superclass field.
      "-Xlint:stars-align",                // Pattern sequence wildcard must align with sequence component.
      "-Xlint:type-parameter-shadow",      // A local type parameter shadows a type already in scope.
      "-Xlint:unsound-match",              // Pattern match may not be typesafe.
      "-Ypartial-unification",             // Enable partial unification in type constructor inference
      "-Ywarn-dead-code",                  // Warn when dead code is identified.
      "-Ywarn-extra-implicit",             // Warn when more than one implicit parameter section is defined.
      "-Ywarn-inaccessible",               // Warn about inaccessible types in method signatures.
      "-Ywarn-infer-any",                  // Warn when a type argument is inferred to be `Any`.
      "-Ywarn-nullary-override",           // Warn when non-nullary `def f()' overrides nullary `def f'.
      "-Ywarn-nullary-unit",               // Warn when nullary methods return Unit.
      "-Ywarn-unused:implicits",           // Warn if an implicit parameter is unused.
      "-Ywarn-unused:imports",             // Warn if an import selector is not referenced.
      "-Ywarn-unused:locals",              // Warn if a local definition is unused.
      "-Ywarn-unused:params",              // Warn if a value parameter is unused.
      "-Ywarn-unused:patvars",             // Warn if a variable bound in a pattern is unused.
      "-Ywarn-unused:privates",            // Warn if a private member is unused.
    ),
    libraryDependencies ++= Seq(
      "org.typelevel"   %% "cats-effect"         % CatsEffectVersion,
      "co.fs2"          %% "fs2-core"            % Fs2Version,

      "org.http4s"      %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s"      %% "http4s-blaze-client" % Http4sVersion,
      "org.http4s"      %% "http4s-circe"        % Http4sVersion,
      "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
      "org.http4s"      %% "http4s-prometheus-metrics"          % Http4sVersion,
      "org.http4s"      %% "http4s-async-http-client"       % Http4sVersion,
      "io.circe"        %% "circe-core"          % CirceVersion,
      "io.circe"        %% "circe-generic"       % CirceVersion,
      "io.circe"        %% "circe-generic-extras"       % CirceVersion,
      "io.circe"        %% "circe-parser"        % CirceVersion,

      "com.h2database"  %  "h2"                  % H2Version,
      "org.flywaydb"    %  "flyway-core"         % FlywayVersion,
      "org.tpolecat"    %% "doobie-core"         % DoobieVersion,
      "org.tpolecat"    %% "doobie-postgres"     % DoobieVersion,
      // doobie connection pooling
      "org.tpolecat"    %% "doobie-hikari"     % DoobieVersion,
      "org.tpolecat"    %% "doobie-h2"           % DoobieVersion,
      "com.typesafe.scala-logging"   %% "scala-logging"                  % "3.9.0",

      "ch.qos.logback"  %  "logback-classic"     % LogbackVersion,
      "com.github.pureconfig"        %% "pureconfig"                     % PureconfigVersion,


      "org.scalatest"   %% "scalatest"           % ScalaTestVersion  % "it,test",
      "org.scalacheck"  %% "scalacheck"          % ScalaCheckVersion % "it,test",
      "org.tpolecat"    %% "doobie-scalatest"    % DoobieVersion % "it,test"
    ),

    cleanFiles += assemblyFolder,
    test in assembly := {},
    assemblyOutputPath in assembly := assemblyFolder / (name.value + "-v" + version.value + ".jar"),
    // Remove resources files from the JAR (they will be copied to an external folder)
    assemblyMergeStrategy in assembly := {
      case PathList("META-INF", _) => MergeStrategy.discard
      case PathList("BUILD") => MergeStrategy.discard
      case path =>  if (ignoreFiles.contains(path))
        MergeStrategy.discard
      else
        (assemblyMergeStrategy in assembly).value(path)
    },
    imageNames in docker := Seq(
      // Sets the latest tag
      ImageName(s"ledgerhq/${name.value}:latest"),

      // Sets a name with a tag that contains the project version
      ImageName(
        namespace = Some("ledgerhq"),
        repository = name.value,
        tag = Some("v" + version.value)
      )
    ),
    // User `docker` to build docker image
    dockerfile in docker := {
      // The assembly task generates a fat JAR file
      val artifact: File = assembly.value
      val artifactTargetPath = s"/app/${artifact.name}"

      new Dockerfile {
        from("openjdk:8-jre")
        add(artifact, artifactTargetPath)
        entryPoint("java", "-jar", artifactTargetPath)
      }
    }
  )
