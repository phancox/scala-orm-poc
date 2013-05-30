package com.dtc.deltasoft.entity

import java.util.Properties
import javax.persistence._
import scala.slick.driver.ExtendedProfile
import scala.slick.session.{ Database, Session }
import scala.slick.driver.{ PostgresDriver => _, _ }
import ch.qos.logback.classic._

object Entity {

  val lc: LoggerContext = org.slf4j.LoggerFactory.getILoggerFactory().asInstanceOf[LoggerContext]
                                                  //> 17:52:52,533 |-INFO in ch.qos.logback.classic.gaffer.ConfigurationDelegate@3
                                                  //| 906173b - Added status listener of type [ch.qos.logback.core.status.OnConsol
                                                  //| eStatusListener]
                                                  //| 17:52:52,806 |-INFO in ch.qos.logback.classic.gaffer.ConfigurationDelegate@3
                                                  //| 906173b - Setting ReconfigureOnChangeFilter scanning period to 15 seconds
                                                  //| 17:52:52,807 |-INFO in ReconfigureOnChangeFilter{invocationCounter=0} - Will
                                                  //|  scan for changes in [[D:\GIT\deltasoft\target\scala-2.10.0\test-classes\log
                                                  //| back.groovy]] every 15 seconds. 
                                                  //| 17:52:52,807 |-INFO in ch.qos.logback.classic.gaffer.ConfigurationDelegate@3
                                                  //| 906173b - Adding ReconfigureOnChangeFilter as a turbo filter
                                                  //| 17:52:52,829 |-INFO in ch.qos.logback.classic.gaffer.ConfigurationDelegate@3
                                                  //| 906173b - About to instantiate turboFilter of type [ch.qos.logback.classic.t
                                                  //| urbo.MDCFilter]
                                                  //| 17:52:52,890 |-INFO in ch.qos.logback.classic.gaffer.ConfigurationDelegate@3
                                                  //| 906173b - Adding aforementioned turbo fil
                                                  //| Output exceeds cutoff limit.
  org.slf4j.LoggerFactory.getLogger("root").asInstanceOf[Logger].setLevel(Level.ERROR)
  org.slf4j.LoggerFactory.getLogger("com.dtc").asInstanceOf[Logger].setLevel(Level.ERROR)
  org.slf4j.LoggerFactory.getLogger("scala.slick.session").asInstanceOf[Logger].setLevel(Level.ERROR)
  org.slf4j.LoggerFactory.getLogger("com.googlecode.mapperdao").asInstanceOf[Logger].setLevel(Level.ERROR)
  org.slf4j.LoggerFactory.getLogger("jdbc.sqltiming").asInstanceOf[Logger].setLevel(Level.ERROR)

  class DAL(override val profile: ExtendedProfile) extends PersonProfile
    with AddressProfile with SuburbProfile with Profile {}
  val dbmsName = "H2" // H2, PostgreSQL           //> dbmsName  : String = H2
  val dbms = dbmsName toLowerCase ()              //> dbms  : String = h2
  implicit val dbConfig = DbConfig(dbms, 2)       //> dbConfig  : com.dtc.deltasoft.entity.DbConfig = DbConfig(h2,2)
  val suburbEntity = new SuburbEntity             //> suburbEntity  : com.dtc.deltasoft.entity.SuburbEntity = SuburbEntity(Suburb
                                                  //| ,com.dtc.deltasoft.entity.Suburb)
  val addressEntity = new AddressEntity           //> addressEntity  : com.dtc.deltasoft.entity.AddressEntity = AddressEntity(Add
                                                  //| ress,com.dtc.deltasoft.entity.Address)
  val personEntity = new PersonEntity             //> personEntity  : com.dtc.deltasoft.entity.PersonEntity = PersonEntity(Person
                                                  //| ,com.dtc.deltasoft.entity.Person)
  val entities = List(suburbEntity, addressEntity, personEntity)
                                                  //> entities  : List[com.dtc.deltasoft.Logging with com.googlecode.mapperdao.En
                                                  //| tity[Int, com.googlecode.mapperdao.SurrogateIntId, _ >: com.dtc.deltasoft.e
                                                  //| ntity.Person with com.dtc.deltasoft.entity.Address with com.dtc.deltasoft.e
                                                  //| ntity.Suburb <: Product with Serializable]] = List(SuburbEntity(Suburb,com.
                                                  //| dtc.deltasoft.entity.Suburb), AddressEntity(Address,com.dtc.deltasoft.entit
                                                  //| y.Address), PersonEntity(Person,com.dtc.deltasoft.entity.Person))
  val ormConnections = getOrmConnections(s"jdbc_${dbms}", entities)
                                                  //> ormConnections  : com.dtc.deltasoft.entity.OrmConnections = OrmConnections(
                                                  //| scala.slick.driver.H2Driver$@27e4203e,scala.slick.session.Database$$anon$1@
                                                  //| 7200dae7,Jdbc(org.apache.tomcat.dbcp.dbcp.BasicDataSource@1623ae2a),MapperD
                                                  //| ao(H2),com.googlecode.mapperdao.jdbc.impl.QueryDaoImpl@7f52c1,org.springfra
                                                  //| mework.jdbc.datasource.DataSourceTransactionManager@2711fd28)
  val slickDb = ormConnections.slickDb            //> slickDb  : scala.slick.session.Database = scala.slick.session.Database$$ano
                                                  //| n$1@7200dae7
  val dal = new DAL(ormConnections.slickDriver)   //> dal  : com.dtc.deltasoft.entity.Entity.DAL = com.dtc.deltasoft.entity.Entit
                                                  //| y$$anonfun$main$1$DAL$1@36c945c3
  import dal.profile.simple._
  val mapperDao = ormConnections.mapperDao        //> mapperDao  : com.googlecode.mapperdao.MapperDao = MapperDao(H2)
  val suburbsDao = new SuburbsDao(ormConnections) //> suburbsDao  : com.dtc.deltasoft.entity.SuburbsDao = com.dtc.deltasoft.entit
                                                  //| y.SuburbsDao@445ef7da
  val addressesDao = new AddressesDao(ormConnections)
                                                  //> addressesDao  : com.dtc.deltasoft.entity.AddressesDao = com.dtc.deltasoft.e
                                                  //| ntity.AddressesDao@69ffdaf8
  val personsDao = new PersonsDao(ormConnections) //> personsDao  : com.dtc.deltasoft.entity.PersonsDao = com.dtc.deltasoft.entit
                                                  //| y.PersonsDao@53546d8b

  slickDb.withSession { implicit session: Session =>
    dal.Suburbs.ddl.create
    dal.Addresses.ddl.create
    dal.Persons.ddl.create
  }

  //************************************************************************************************
  
  val person = Person("Hancox", "Peter",
    Address("46 Dettmann Avenue", null, Suburb("Longueville", "2066", "NSW", "Australia")),
    Address("PO Box 1383", null, Suburb("Lane Cove", "1595", "NSW", "Australia")))
                                                  //> person  : com.dtc.deltasoft.entity.Person = Hancox, Peter

  val inserted = personsDao.create(person)        //> inserted  : com.dtc.deltasoft.entity.Person with com.googlecode.mapperdao.S
                                                  //| urrogateIntId = Hancox, Peter
  val selected = personsDao.retrieve(inserted.id).get
                                                  //> selected  : com.dtc.deltasoft.entity.Person with com.googlecode.mapperdao.S
                                                  //| urrogateIntId = Hancox, Peter

  suburbsDao.page(1, 10).mkString("\n", "\n", "\n")
                                                  //> res0: String = "
                                                  //| Longueville, NSW 2066, Australia
                                                  //| Lane Cove, NSW 1595, Australia
                                                  //| "
  addressesDao.page(1, 10).mkString("\n", "\n", "\n")
                                                  //> res1: String = "
                                                  //| 46 Dettmann Avenue, Longueville, NSW 2066, Australia
                                                  //| PO Box 1383, Lane Cove, NSW 1595, Australia
                                                  //| "
  personsDao.page(1, 10).mkString("\n", "\n", "\n")
                                                  //> res2: String = "
                                                  //| Hancox, Peter
                                                  //| "
}