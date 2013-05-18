package com.dtc.deltasoft.entity

import java.util.Properties
import javax.persistence._
import scala.slick.driver.ExtendedProfile
import scala.slick.session.{ Database, Session }
import scala.slick.driver.{ PostgresDriver => _, _ }
import ch.qos.logback.classic._

object Entity {

  val lc: LoggerContext = org.slf4j.LoggerFactory.getILoggerFactory().asInstanceOf[LoggerContext]
                                                  //> 01:20:03,156 |-INFO in ch.qos.logback.classic.gaffer.ConfigurationDelegate@6
                                                  //| 51cf5b3 - Added status listener of type [ch.qos.logback.core.status.OnConsol
                                                  //| eStatusListener]
                                                  //| 01:20:03,466 |-INFO in ch.qos.logback.classic.gaffer.ConfigurationDelegate@6
                                                  //| 51cf5b3 - Setting ReconfigureOnChangeFilter scanning period to 15 seconds
                                                  //| 01:20:03,467 |-INFO in ReconfigureOnChangeFilter{invocationCounter=0} - Will
                                                  //|  scan for changes in [[Q:\GIT\deltasoft\target\scala-2.10.0\test-classes\log
                                                  //| back.groovy]] every 15 seconds. 
                                                  //| 01:20:03,467 |-INFO in ch.qos.logback.classic.gaffer.ConfigurationDelegate@6
                                                  //| 51cf5b3 - Adding ReconfigureOnChangeFilter as a turbo filter
                                                  //| 01:20:03,578 |-INFO in ch.qos.logback.classic.gaffer.ConfigurationDelegate@6
                                                  //| 51cf5b3 - About to instantiate turboFilter of type [ch.qos.logback.classic.t
                                                  //| urbo.MDCFilter]
                                                  //| 01:20:03,848 |-INFO in ch.qos.logback.classic.gaffer.ConfigurationDelegate@6
                                                  //| 51cf5b3 - Adding aforementioned turbo fil
                                                  //| Output exceeds cutoff limit.
  org.slf4j.LoggerFactory.getLogger("root").asInstanceOf[Logger].setLevel(Level.OFF)
  org.slf4j.LoggerFactory.getLogger("com.dtc").asInstanceOf[Logger].setLevel(Level.OFF)

  class DAL(override val profile: ExtendedProfile) extends PersonProfile
    with AddressProfile with SuburbProfile with Profile {}
  val dbmsName = "H2" // H2, PostgreSQL           //> dbmsName  : String = H2
  implicit val dbms = dbmsName toLowerCase ()     //> dbms  : String = h2
  val suburbEntity = new SuburbEntity             //> suburbEntity  : com.dtc.deltasoft.entity.SuburbEntity = SuburbEntity(Suburb,
                                                  //| com.dtc.deltasoft.entity.Suburb)
  val addressEntity = new AddressEntity           //> addressEntity  : com.dtc.deltasoft.entity.AddressEntity = AddressEntity(Addr
                                                  //| ess,com.dtc.deltasoft.entity.Address)
  val personEntity = new PersonEntity             //> personEntity  : com.dtc.deltasoft.entity.PersonEntity = PersonEntity(Person,
                                                  //| com.dtc.deltasoft.entity.Person)
  val entities = List(suburbEntity, addressEntity, personEntity)
                                                  //> entities  : List[com.googlecode.mapperdao.Entity[Int, com.googlecode.mapperd
                                                  //| ao.SurrogateIntId, _ >: com.dtc.deltasoft.entity.Person with com.dtc.deltaso
                                                  //| ft.entity.Address with com.dtc.deltasoft.entity.Suburb <: Product with Seria
                                                  //| lizable] with grizzled.slf4j.Logging] = List(SuburbEntity(Suburb,com.dtc.del
                                                  //| tasoft.entity.Suburb), AddressEntity(Address,com.dtc.deltasoft.entity.Addres
                                                  //| s), PersonEntity(Person,com.dtc.deltasoft.entity.Person))
  val ormConnections = getOrmConnections(entities, dbms)
                                                  //> ormConnections  : com.dtc.deltasoft.entity.OrmConnections = OrmConnections(
                                                  //| scala.slick.driver.H2Driver$@2183efa2,scala.slick.session.Database$$anon$1@
                                                  //| 5ce985de,Jdbc(org.apache.tomcat.dbcp.dbcp.BasicDataSource@6753e179),MapperD
                                                  //| ao(H2),com.googlecode.mapperdao.jdbc.impl.QueryDaoImpl@33f803ee,org.springf
                                                  //| ramework.jdbc.datasource.DataSourceTransactionManager@5fca18e9)
  val slickDb = ormConnections.slickDb            //> slickDb  : scala.slick.session.Database = scala.slick.session.Database$$ano
                                                  //| n$1@5ce985de
  val dal = new DAL(ormConnections.slickDriver)   //> dal  : com.dtc.deltasoft.entity.Entity.DAL = com.dtc.deltasoft.entity.Entit
                                                  //| y$$anonfun$main$1$DAL$1@28f1a48
  import dal.profile.simple._
  val mapperDao = ormConnections.mapperDao        //> mapperDao  : com.googlecode.mapperdao.MapperDao = MapperDao(H2)
  val suburbsDao = new SuburbsDao(ormConnections) //> suburbsDao  : com.dtc.deltasoft.entity.SuburbsDao = com.dtc.deltasoft.entit
                                                  //| y.SuburbsDao@1c85f6f5
  val addressesDao = new AddressesDao(ormConnections)
                                                  //> addressesDao  : com.dtc.deltasoft.entity.AddressesDao = com.dtc.deltasoft.e
                                                  //| ntity.AddressesDao@5ec433e2
  val personsDao = new PersonsDao(ormConnections) //> personsDao  : com.dtc.deltasoft.entity.PersonsDao = com.dtc.deltasoft.entit
                                                  //| y.PersonsDao@352ef9c8

  slickDb.withSession { implicit session: Session =>
    dal.Suburbs.ddl.create
    dal.Addresses.ddl.create
    dal.Persons.ddl.create
  }                                               //> 01:20:12.889 [main] DEBUG scala.slick.session.BaseSession - Preparing state
                                                  //| ment: create table "SUBURB" ("ID" INTEGER GENERATED BY DEFAULT AS IDENTITY(
                                                  //| START WITH 1) NOT NULL PRIMARY KEY,"NAME" VARCHAR NOT NULL,"POSTCODE" VARCH
                                                  //| AR NOT NULL,"STATE" VARCHAR NOT NULL,"COUNTRY" VARCHAR NOT NULL)
                                                  //| 01:20:12.934 [main] DEBUG jdbc.sqltiming -  org.apache.tomcat.dbcp.dbcp.Del
                                                  //| egatingPreparedStatement.execute(DelegatingPreparedStatement.java:172)
                                                  //| 2. create table "SUBURB" ("ID" INTEGER GENERATED BY DEFAULT AS IDENTITY(STA
                                                  //| RT WITH 1) NOT NULL 
                                                  //| PRIMARY KEY,"NAME" VARCHAR NOT NULL,"POSTCODE" VARCHAR NOT NULL,"STATE" VAR
                                                  //| CHAR NOT NULL,"COUNTRY" 
                                                  //| VARCHAR NOT NULL) 
                                                  //|  {executed in 24 msec}
                                                  //| 01:20:13.085 [main] DEBUG scala.slick.session.BaseSession - Preparing state
                                                  //| ment: create table "ADDRESS" ("ID" INTEGER GENERATED BY DEFAULT AS IDENTITY
                                                  //| (START WITH 1) NOT NULL PRIMARY KEY,"STREET_1" VARCHAR NOT NULL,"STREET_2" 
                                                  //| VARCHAR,"
                                                  //| Output exceeds cutoff limit.

  //************************************************************************************************

  val person = Person("Hancox", "Peter",
    Address("46 Dettmann Avenue", null, Suburb("Longueville", "2066", "NSW", "Australia")),
    Address("PO Box 1383", null, Suburb("Lane Cove", "1595", "NSW", "Australia")))
                                                  //> person  : com.dtc.deltasoft.entity.Person = Hancox, Peter

  val inserted = personsDao.create(person)        //> 01:20:13.759 [main] DEBUG com.googlecode.mapperdao.jdbc.Jdbc - batchUpdate
                                                  //| ----------------- start of batch -------------------------------------
                                                  //| insert into Suburb(NAME,POSTCODE,STATE,COUNTRY) values('Longueville','2066'
                                                  //| ,'NSW','Australia')
                                                  //| insert into Suburb(NAME,POSTCODE,STATE,COUNTRY) values('Lane Cove','1595','
                                                  //| NSW','Australia')
                                                  //| ----------------- end of batch ---------------------------------------
                                                  //| 01:20:13.803 [main] DEBUG jdbc.sqltiming -  org.apache.tomcat.dbcp.dbcp.Del
                                                  //| egatingPreparedStatement.executeUpdate(DelegatingPreparedStatement.java:105
                                                  //| )
                                                  //| 2. insert into Suburb(NAME,POSTCODE,STATE,COUNTRY) values('Longueville','20
                                                  //| 66','NSW','Australia') 
                                                  //|  {executed in 8 msec}
                                                  //| 01:20:13.860 [main] DEBUG jdbc.sqltiming -  org.apache.tomcat.dbcp.dbcp.Del
                                                  //| egatingPreparedStatement.executeUpdate(DelegatingPreparedStatement.java:105
                                                  //| )
                                                  //| 2. insert into Suburb(NAME,POSTCODE,STATE,COUNTRY) values('Lane Cove','159
                                                  //| Output exceeds cutoff limit.
  val selected = personsDao.retrieve(inserted.id).get
                                                  //> 01:20:14.069 [main] DEBUG com.googlecode.mapperdao.jdbc.Jdbc - sql:
                                                  //| select ID,SURNAME,FIRST_NAME,HOME_ADDRESS__ID,WORK_ADDRESS__ID
                                                  //| from Person 
                                                  //| where ID = 1
                                                  //| 
                                                  //| 01:20:14.097 [main] DEBUG jdbc.sqltiming -  org.apache.tomcat.dbcp.dbcp.Del
                                                  //| egatingPreparedStatement.executeQuery(DelegatingPreparedStatement.java:96)
                                                  //| 
                                                  //| 2. select ID,SURNAME,FIRST_NAME,HOME_ADDRESS__ID,WORK_ADDRESS__ID from Pers
                                                  //| on where ID = 1 
                                                  //|  {executed in 1 msec}
                                                  //| 01:20:14.249 [main] DEBUG com.googlecode.mapperdao.jdbc.Jdbc - sql:
                                                  //| select ID,STREET_1,STREET_2,SUBURB__ID
                                                  //| from Address 
                                                  //| where ID = 1
                                                  //| 
                                                  //| 01:20:14.250 [main] DEBUG jdbc.sqltiming -  org.apache.tomcat.dbcp.dbcp.Del
                                                  //| egatingPreparedStatement.executeQuery(DelegatingPreparedStatement.java:96)
                                                  //| 
                                                  //| 2. select ID,STREET_1,STREET_2,SUBURB__ID from Address where ID = 1 
                                                  //|  {executed in 0 msec}
                                                  //| 01:20:14.252 [main] DEBUG com.googlecode.mapperdao.jdbc.Jdbc - sql:
                                                  //| select I
                                                  //| Output exceeds cutoff limit.

}