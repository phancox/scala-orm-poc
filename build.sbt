name := "deltasoft"
 
organization := "com.dtc"

version := "1.0.0-SNAPSHOT"

homepage := Some(url("http://www.dtc.com.au"))

description := "DeltaSoft framework version 2."
 
scalaVersion := "2.10.0"

javacOptions ++= Seq("-source", "1.6", "-target", "1.6")

resolvers += "Vaadin add-ons repository" at "http://maven.vaadin.com/vaadin-addons"

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.10" % "2.0.M5b" % "test",
  "junit" % "junit" % "4.10" % "test"
)

libraryDependencies ++= Seq(
  "org.clapper" % "grizzled-slf4j_2.10" % "1.0.1",
  "ch.qos.logback" % "logback-classic" % "1.0.9",
  "org.codehaus.janino" % "janino" % "2.6.1",
  "org.codehaus.groovy" % "groovy-all" % "2.1.1",
  "vaadin.scala" % "scaladin_2.10" % "3.0.0-SNAPSHOT",
  "com.vaadin" % "vaadin-themes" % "7.0.1",
  "com.vaadin" % "vaadin-client-compiled" % "7.0.1",
  "org.hibernate.javax.persistence" % "hibernate-jpa-2.0-api" % "1.0.1.Final",
  "org.hibernate" % "hibernate-entitymanager" % "4.1.9.Final",
  "postgresql" % "postgresql" % "9.1-901.jdbc4"
)
