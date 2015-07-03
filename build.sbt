import sbt._
import Keys._
import Common._
import Dependencies._

val gitHeadCommitSha = settingKey[String]("SHA of latest git HEAD commit.")

gitHeadCommitSha in ThisBuild := Process("git rev-parse HEAD").lines.head

version in ThisBuild := s"1.0-${gitHeadCommitSha.value}"

isSnapshot in ThisBuild := true

scalaVersion := "2.10.4"

EclipseKeys.skipParents in ThisBuild := false

lazy val deltasoftCore = (
  DeltasoftProject("deltasoft-core")
  settings(libraryDependencies ++= coreDependencies)
)

lazy val deltasoftDatabase = (
  DeltasoftProject("deltasoft-database")
  dependsOn(deltasoftCore)
  settings(libraryDependencies ++= databaseDependencies)
)

lazy val deltasoftVaadin = (
  DeltasoftProject("deltasoft-vaadin")
  dependsOn(deltasoftCore)
  settings(
    resolvers ++= vaadinResolvers,
    libraryDependencies ++= vaadinDependencies
  )
)
