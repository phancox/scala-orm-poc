package com.dtc.deltasoft.entity

import java.util.Properties
import javax.persistence._
import slick.driver.ExtendedProfile
import slick.session.{ Database, Session }
import slick.driver.{ PostgresDriver => _, _ }
import ch.qos.logback.classic._

object Entity {

  val lc: LoggerContext = org.slf4j.LoggerFactory.getILoggerFactory().asInstanceOf[LoggerContext]
  org.slf4j.LoggerFactory.getLogger("root").asInstanceOf[Logger].setLevel(Level.ERROR)
  org.slf4j.LoggerFactory.getLogger("com.dtc").asInstanceOf[Logger].setLevel(Level.ERROR)
  org.slf4j.LoggerFactory.getLogger("slick.session").asInstanceOf[Logger].setLevel(Level.ERROR)
  org.slf4j.LoggerFactory.getLogger("com.googlecode.mapperdao").asInstanceOf[Logger].setLevel(Level.ERROR)
  org.slf4j.LoggerFactory.getLogger("jdbc.sqltiming").asInstanceOf[Logger].setLevel(Level.ERROR)

  class DAL(override val profile: ExtendedProfile) extends PersonProfile
    with AddressProfile with SuburbProfile with Profile {}
  val dbmsName = "H2" // H2, PostgreSQL
  val dbms = dbmsName toLowerCase ()
  implicit val dbConfig = DbConfig(dbms, 2)
  val suburbEntity = new SuburbEntity
  val addressEntity = new AddressEntity
  val personEntity = new PersonEntity
  val entities = List(suburbEntity, addressEntity, personEntity)
  val ormConnections = getOrmConnections(s"jdbc_${dbms}", entities)
  val slickDb = ormConnections.slickDb
  val dal = new DAL(ormConnections.slickDriver)
  import dal.profile.simple._
  val mapperDao = ormConnections.mapperDao
  val suburbsDao = new SuburbsDao(ormConnections)
  val addressesDao = new AddressesDao(ormConnections)
  val personsDao = new PersonsDao(ormConnections)

  slickDb.withSession { implicit session: Session =>
    dal.Suburbs.ddl.create
    dal.Addresses.ddl.create
    dal.Persons.ddl.create
  }

  //************************************************************************************************
  
  val person = Person("Hancox", "Peter",
    Address("46 Dettmann Avenue", null, Suburb("Longueville", "2066", "NSW", "Australia")),
    Address("PO Box 1383", null, Suburb("Lane Cove", "1595", "NSW", "Australia")))

  val inserted = personsDao.create(person)
  val selected = personsDao.retrieve(inserted.id).get

  suburbsDao.page(1, 10).mkString("\n", "\n", "\n")
  addressesDao.page(1, 10).mkString("\n", "\n", "\n")
  personsDao.page(1, 10).mkString("\n", "\n", "\n")
}