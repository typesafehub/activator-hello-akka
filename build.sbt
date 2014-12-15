name := "hello-akka"

version := "15v01p01"

libraryDependencies ++= Seq(
  TypesafeLibrary.akkaActor.value,
  TypesafeLibrary.akkaTestkit.value % "test",
  "org.scalatest" %% "scalatest" % "2.1.6" % "test",
  "junit" % "junit" % "4.11" % "test",
  "com.novocode" % "junit-interface" % "0.10" % "test"
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")
