name := "RuleBook"

version := "1.0"

scalaVersion := "2.11.2"

libraryDependencies ++= 
  Seq( "org.scalatest" % "scalatest_2.11" % "2.1.7",
       "org.scala-lang" % "scala-compiler" % scalaVersion.value,
       "org.scalafx" % "scalafx_2.11" % "8.0.5-R5" )

scalacOptions += "-deprecation"
