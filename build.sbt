lazy val root = (project in file("."))
  .enablePlugins(PlayJava) // Ensures PlayJava plugin is enabled.
  .settings(
    name := "ITSD Card Game 23-24",
    version := "1.1",
    scalaVersion := "2.13.1",

    // Adding test options for JUnit
    testOptions += Tests.Argument(TestFrameworks.JUnit, "-a", "-v"),

    // Defining libraryDependencies in a single assignment
    libraryDependencies ++= Seq(
      guice,
      ws,
      "org.webjars" %% "webjars-play" % "2.8.0",
      "org.webjars" % "bootstrap" % "2.3.2",
      "org.webjars" % "flot" % "0.8.3",
      "org.assertj" % "assertj-core" % "3.14.0" % Test,
      "org.awaitility" % "awaitility" % "4.0.1" % Test,
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.10.3",
      "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % "2.10.3",
      "junit" % "junit" % "4.13.2",
      "com.novocode" % "junit-interface" % "0.11" % Test exclude("junit", "junit-dep"),
      "com.typesafe.akka" %% "akka-actor-typed" % "2.6.14" // Ensure this is correctly placed within the sequence
    ),

    // Dependency overrides
    dependencyOverrides ++= Seq(
      "commons-codec" % "commons-codec" % "1.6",
      "commons-io" % "commons-io" % "2.1"
    ),

    // Compiler options
    javacOptions ++= Seq(
      "-Xlint:unchecked",
      "-Xlint:deprecation",
      "-Werror"
    )
  )
