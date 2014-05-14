import sbt._
import Keys._
import com.typesafe.sbteclipse.plugin.EclipsePlugin._

object Common {

  val commonSettings = Seq(
    organization := "com.dtc",
    homepage := Some(url("http://www.dtc.com.au")),
    description := "DeltaSoft Technical Architecture Framework (2.x)",
	scalaVersion := "2.10.4",
    javacOptions ++= Seq("-source", "1.6", "-target", "1.6"),
    scalacOptions ++= Seq("-deprecation", "-feature", "-language:implicitConversions", "-language:postfixOps"),
    EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource,
    EclipseKeys.withSource := true
  )
  
  def DeltasoftProject(name: String) = (
    Project(name, file(name))
    settings(commonSettings:_*)
  )
}