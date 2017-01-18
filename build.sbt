import com.typesafe.sbt.SbtNativePackager.autoImport._

import sbt.Keys._

import sbt._

name := "extractor"

organization := "org.comp.bio.aging"

scalaVersion :=  "2.12.1"

scalacOptions ++= Seq( "-target:jvm-1.8", "-feature", "-language:_" )

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint", "-J-Xss5M", "-encoding", "UTF-8")

mainClass in Compile := Some("org.comp.bio.aging.extractor.Main")

unmanagedClasspath in Compile ++= (unmanagedResources in Compile).value

updateOptions := updateOptions.value.withCachedResolution(true) //to speed up dependency resolution

resolvers += sbt.Resolver.bintrayRepo("comp-bio-aging", "main")

resolvers += sbt.Resolver.bintrayRepo("denigma", "denigma-releases")

resolvers += "ICM repository" at "http://maven.icm.edu.pl/artifactory/repo"

libraryDependencies ++= Seq(
  "com.github.melrief" %% "purecsv" % "0.0.9",
  "com.lihaoyi" %% "pprint" % "0.4.4",
  "com.lihaoyi" %% "fastparse" % "0.4.1"
  "pl.edu.icm.cermine" % "cermine-impl" % "1.11",
  "com.github.pathikrit" %% "better-files" % "2.17.1",
  "edu.eckerd" %% "google-api-scala" % "0.1.0",
  "org.backuity.clist" %% "clist-core"   % "3.2.2",
  "org.backuity.clist" %% "clist-macros" % "3.2.2" % "provided",
  "org.scalatest" %% "scalatest" % "3.0.1" % Test
)

libraryDependencies += "com.lihaoyi" % "ammonite" % "0.8.1" % Test cross CrossVersion.full

initialCommands in (Test, console) := """ammonite.Main().run()"""

exportJars := true

fork in run := true

parallelExecution in Test := false

maintainer := "Anton Kulaga <antonkulaga@gmail.com>"

packageSummary := "reference-extractor"

packageDescription := """Extractor of references from the PDF"""

bintrayRepository := "main"

bintrayOrganization := Some("comp-bio-aging")

licenses += ("MPL-2.0", url("http://opensource.org/licenses/MPL-2.0"))

enablePlugins(JavaAppPackaging)