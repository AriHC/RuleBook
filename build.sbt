name := "RuleBook"

version := "1.0"

scalaVersion := "2.10.3"

libraryDependencies ++= 
  Seq( "org.scalatest" % "scalatest_2.10" % "2.1.7",
       "org.scala-lang" % "scala-compiler" % scalaVersion.value )

scalacOptions += "-deprecation"
