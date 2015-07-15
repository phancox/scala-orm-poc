import sbt._
import Keys._
import com.typesafe.sbteclipse.plugin.EclipsePlugin._

object Common {

  val commonSettings = Seq(
    organization := "com.dtc",
    homepage := Some(url("http://www.dtc.com.au")),
    description := "DeltaSoft Technology Framework",
    scalaVersion := "2.11.7",
    javacOptions ++= Seq("-source", "1.6", "-target", "1.6"),
    scalacOptions ++= Seq("-deprecation", "-feature", "-language:implicitConversions", "-language:postfixOps"),
    libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test",
    libraryDependencies += "junit" % "junit" % "4.10" % "test",
    EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource,
    EclipseKeys.withSource := true
  )
  
  def DeltasoftProject(name: String) = (
    Project(name, file(name))
    settings(commonSettings:_*)
  )
}