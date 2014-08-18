name := "hello-akka"

version := "1.0"

scalaVersion := "2.11.2"

val akkaVersion = "2.3.5"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor"      % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit"    % akkaVersion,
  "org.scalatest"     %% "scalatest"       % "2.2.1" % "test",
  "com.novocode"      %  "junit-interface" % "0.11"  % "test"
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")
