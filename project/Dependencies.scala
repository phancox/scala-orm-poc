import sbt._
import Keys._

object Dependencies {

  // ***********************************************************************************************
  // EXTERNAL SUPPORTING LIBRARIES
  // ***********************************************************************************************
  val vaadinVersion = "7.3.5"
  
  val grizzledSlf4j = "org.clapper" %% "grizzled-slf4j" % "1.0.2"
  val logbackClassic = "ch.qos.logback" % "logback-classic" % "1.1.2"
  val janino = "org.codehaus.janino" % "janino" % "2.6.1"
  val groovyAll = "org.codehaus.groovy" % "groovy-all" % "2.1.1"
  
  val commonsConfiguration = "commons-configuration" % "commons-configuration" % "1.10"
  val commonsCollections = "commons-collections" % "commons-collections" % "3.2.1"
  val commonsBeanutils = "commons-beanutils" % "commons-beanutils" % "1.8.3"
  val jodaTime = "joda-time" % "joda-time" % "2.2"
  val jodaConvert = "org.joda" % "joda-convert" % "1.3.1"
  val nscalaTime = "com.github.nscala-time" %% "nscala-time" % "0.4.2"
  val springScala = "org.springframework.scala" % "spring-scala" % "1.0.0.M2"
  val scalaequalsCore = "org.scalaequals" %% "scalaequals-core" % "1.2.0"
  val usertypeJodatime = "org.jadira.usertype" % "usertype.jodatime" % "2.0.1"

  val h2 = "com.h2database" % "h2" % "1.3.170"
  val postgresql = "org.postgresql" % "postgresql" % "9.3-1101-jdbc41"
  val log4jdbc = "org.bgee.log4jdbc-log4j2" % "log4jdbc-log4j2-jdbc4.1" % "1.16"
  val slick = "com.typesafe.slick" %% "slick" % "1.0.0"
  val hibernateJpa = "org.hibernate.javax.persistence" % "hibernate-jpa-2.0-api" % "1.0.1.Final"
  val hibernateEntitymanager = "org.hibernate" % "hibernate-entitymanager" % "4.1.9.Final"
  val springContext = "org.springframework" % "spring-context" % "3.2.2.RELEASE"
  val springJdbc = "org.springframework" % "spring-jdbc" % "3.2.2.RELEASE"
  val mapperdao = "com.googlecode.mapperdao" % "mapperdao" % "1.0.0.rc23-2.10.1"

  val servletApi = "javax.servlet" % "javax.servlet-api" % "3.1.0"
  val tomcatDbcp = "org.apache.tomcat" % "tomcat-dbcp" % "7.0.39"
  val vaadinServer = "com.vaadin" % "vaadin-server" % vaadinVersion
  val vaadinShared = "com.vaadin" % "vaadin-shared" % vaadinVersion
  val vaadinClientCompiled = "com.vaadin" % "vaadin-client-compiled" % vaadinVersion
  val vaadinThemes = "com.vaadin" % "vaadin-themes" % vaadinVersion
  val scaladin = "vaadin.scala" %% "scaladin" % "3.1-SNAPSHOT"
  
  val vaadinResolvers = Seq(
    "Vaadin add-ons repository" at "http://maven.vaadin.com/vaadin-addons",
    "Scaladin Snapshots" at "http://henrikerola.github.io/repository/snapshots/"
  )    
  
  // ***********************************************************************************************
  // PROJECT DEPENDENCIES
  // ***********************************************************************************************
  
  val coreDependencies = Seq(
    grizzledSlf4j,
    logbackClassic,
    janino,
    groovyAll,
    commonsConfiguration,
    commonsCollections,
    commonsBeanutils,
    jodaTime,
    jodaConvert,
    nscalaTime,
    scalaequalsCore,
    usertypeJodatime
  )
  
  val databaseDependencies = Seq(
    h2,
    postgresql,
    tomcatDbcp,
    log4jdbc,
    slick,
    hibernateJpa,
    hibernateEntitymanager,
    springContext,
    springJdbc,
    mapperdao
  )
  
  val vaadinDependencies = Seq(
    servletApi,
    vaadinServer,
    vaadinShared,
    vaadinClientCompiled,
    vaadinThemes,
    scaladin
  )
}
