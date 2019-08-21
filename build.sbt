name := "pripyat"

version := "0.1"

scalaVersion := "2.12.6"

val akkaVersion = "2.5.19"
val akkaHttpVersion = "10.1.8"
val nemSdkVersion = "0.9.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http"   % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-actor"  % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "io.nem" % "sdk" % nemSdkVersion,
)
