name := "deltasoft"

organization := "com.dtc"

version := "1.0.0-SNAPSHOT"

homepage := Some(url("http://www.dtc.com.au"))

description := "DeltaSoft Technical Architecture Framework (2.x)"

scalaVersion := "2.10.4"

javacOptions ++= Seq("-source", "1.6", "-target", "1.6")

resolvers += "sonatype.releases" at "http://oss.sonatype.org/content/repositories/releases/"

resolvers += "sonatype.snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

resolvers += "Vaadin add-ons repository" at "http://maven.vaadin.com/vaadin-addons"

resolvers += "Scaladin Snapshots" at "http://henrikerola.github.io/repository/snapshots/"

resolvers += "SpringSource repository" at "http://repo.springsource.org/libs-milestone"

libraryDependencies ++= Seq(
  "org.clapper" % "grizzled-slf4j_2.10" % "1.0.1",
  "ch.qos.logback" % "logback-classic" % "1.0.9",
  "org.codehaus.janino" % "janino" % "2.6.1",
  "org.codehaus.groovy" % "groovy-all" % "2.1.1",
  "commons-configuration" % "commons-configuration" % "1.9",
  "commons-collections" % "commons-collections" % "3.2.1",
  "commons-beanutils" % "commons-beanutils" % "1.8.3",
  "joda-time" % "joda-time" % "2.2",
  "org.joda" % "joda-convert" % "1.3.1",
  "com.github.nscala-time" %% "nscala-time" % "0.4.2",
  "org.springframework" % "spring-context" % "3.2.2.RELEASE",
  "org.springframework" % "spring-jdbc" % "3.2.2.RELEASE",
  "org.springframework.scala" % "spring-scala" % "1.0.0.M2",
  "org.apache.tomcat" % "tomcat-dbcp" % "7.0.39",
  "com.typesafe.slick" %% "slick" % "1.0.0",
  "com.googlecode.mapperdao" % "mapperdao" % "1.0.0.rc23-2.10.1",
  "org.scalaequals" %% "scalaequals-core" % "1.2.0",
  "vaadin.scala" %% "scaladin" % "3.0-SNAPSHOT",
  "javax.servlet" % "javax.servlet-api" % "3.1.0",
  "com.vaadin" % "vaadin-server" % "7.1.14",
  "com.vaadin" % "vaadin-shared" % "7.1.14",
  "com.vaadin" % "vaadin-client-compiled" % "7.1.14",
  "com.vaadin" % "vaadin-themes" % "7.1.14",
  "org.hibernate.javax.persistence" % "hibernate-jpa-2.0-api" % "1.0.1.Final",
  "org.hibernate" % "hibernate-entitymanager" % "4.1.9.Final",
  "org.postgresql" % "postgresql" % "9.3-1101-jdbc41"
)

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.10" % "2.0.M5b" % "test",
  "junit" % "junit" % "4.10" % "test",
  "com.h2database" % "h2" % "1.3.170" % "test",
  "org.jadira.usertype" % "usertype.jodatime" % "2.0.1"
)

scalacOptions ++= Seq("-deprecation", "-feature", "-language:implicitConversions", "-language:postfixOps")
