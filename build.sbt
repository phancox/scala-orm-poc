name := "deltasoft"
 
scalaVersion := "2.10.0"
 
seq(webSettings: _*)

resolvers += "Vaadin add-ons repository" at "http://maven.vaadin.com/vaadin-addons"

libraryDependencies ++= Seq(
  "org.clapper" % "grizzled-slf4j_2.10" % "1.0.1",
  "ch.qos.logback" % "logback-classic" % "1.0.9",
  "org.codehaus.janino" % "janino" % "2.6.1",
  "org.codehaus.groovy" % "groovy-all" % "2.1.1",
  "vaadin.scala" % "scaladin_2.10" % "3.0.0-SNAPSHOT",
  "com.vaadin" % "vaadin-themes" % "7.0.1",
  "com.vaadin" % "vaadin-client-compiled" % "7.0.1",
  "javax.persistence" % "persistence-api" % "1.0.2",
  "org.hibernate" % "hibernate-entitymanager" % "4.1.9.Final",
  "postgresql" % "postgresql" % "9.1-901.jdbc4",
  "org.eclipse.jetty" % "jetty-webapp" % "8.0.4.v20111024" % "container"
)
