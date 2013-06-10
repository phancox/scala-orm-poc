package com.dtc.deltasoft.entity

import scala.slick.driver.ExtendedProfile
import scala.slick.driver.{ PostgresDriver => _, _ }
import scala.slick.session.{ Database, Session }
import javax.persistence._
import java.util.Properties

import com.dtc.deltasoft.Config._
import org.apache.commons.configuration.PropertiesConfiguration
import org.joda.time._

import org.junit.runner.RunWith
import org.scalatest.FunSpec
import org.scalatest.junit._
import org.scalatest.matchers.ShouldMatchers

/**
 * Unit test suite for the [[Person]] entity.
 *
 */
@RunWith(classOf[JUnitRunner])
class PersonSpec extends FunSpec with ShouldMatchers {

  class DAL(override val profile: ExtendedProfile) extends PersonProfile
    with AddressProfile with SuburbProfile with Profile {}
  val jdbcDbManager = com.dtc.deltasoft.entity.jdbcDbManager
  implicit val dbConfig = DbConfig(2)
  val suburbEntity = new SuburbEntity
  val addressEntity = new AddressEntity
  val personEntity = new PersonEntity
  val entities = List(suburbEntity, addressEntity, personEntity)
  val ormConnections = getOrmConnections(entities)
  val slickDb = ormConnections.slickDb
  val dal = new DAL(ormConnections.slickDriver)
  import dal.profile.simple._
  val mapperDao = ormConnections.mapperDao
  val suburbDao = new SuburbDao(ormConnections)
  val addressDao = new AddressDao(ormConnections)
  val personDao = new PersonDao(ormConnections)

  lazy val emf = Persistence.createEntityManagerFactory(jdbcDbManager)
  lazy val entityManager = emf.createEntityManager()

  var person1: Person = _

  describe("A Person entity") {
    describe("should support being created") {
      it("using an empty constructor with properties set using JavaBean modifiers") {
        val person = Person()
        person.setSurname("Hancox")
        person.setFirstName("Peter")
        person.setDateOfBirth(new LocalDate(1965, 2, 12))
        person.setHomeAddress(Address("46 Dettmann Avenue", null, Suburb("Longueville", "2066", "NSW", "Australia")))
        person.setWorkAddress(Address("PO Box 1383", null, Suburb("Lane Cove", "1595", "NSW", "Australia")))
        person should have(
          'surname("Hancox"),
          'firstName("Peter"),
          'dateOfBirth(new LocalDate(1965, 2, 12)),
          'homeAddress(Address("46 Dettmann Avenue", null, Suburb("Longueville", "2066", "NSW", "Australia"))),
          'workAddress(Address("PO Box 1383", null, Suburb("Lane Cove", "1595", "NSW", "Australia"))))
        person.toString should equal("Hancox, Peter")
        person1 = person
      }
      it("using a constructor with a named parameter list") {
        val person = Person(
          surname = "Hancox",
          firstName = "Peter",
          dateOfBirth = new LocalDate(1965, 2, 12),
          homeAddress = Address("46 Dettmann Avenue", null, Suburb("Longueville", "2066", "NSW", "Australia")),
          workAddress = Address("PO Box 1383", null, Suburb("Lane Cove", "1595", "NSW", "Australia")))
        person.toString should equal("Hancox, Peter")
      }
      it("using a constructor with positional parameters") {
        val person = Person("Hancox", "Peter", new LocalDate(1965, 2, 12),
          Address("46 Dettmann Avenue", null, Suburb("Longueville", "2066", "NSW", "Australia")),
          Address("PO Box 1383", null, Suburb("Lane Cove", "1595", "NSW", "Australia")))
        person.toString should equal("Hancox, Peter")
      }
    }
    it("should support equality checks") {
      val person = Person()
      person.setSurname("Hancox")
      person.setFirstName("Peter")
      person.setDateOfBirth(new LocalDate(1965, 2, 12))
      person.setHomeAddress(Address("46 Dettmann Avenue", null, Suburb("Longueville", "2066", "NSW", "Australia")))
      person.setWorkAddress(Address("PO Box 1383", null, Suburb("Lane Cove", "1595", "NSW", "Australia")))
      person should equal(person1)
    }
    describe(s"should support ${jdbcDbManager} schema updates via Slick including") {
      it("table creation") {
        slickDb.withSession { implicit session: Session =>
          try {
            dal.Persons.ddl.drop
            dal.Addresses.ddl.drop
            dal.Suburbs.ddl.drop
          } catch {
            case _: Throwable =>
          }
          dal.Suburbs.ddl.create
          dal.Addresses.ddl.create
          dal.Persons.ddl.create
        }
      }
    }
    describe(s"should support ${jdbcDbManager} persistance via MapperDao including") {
      it("database persistence via mapperDao") {
        mapperDao.insert(personEntity, person1)
      }
      it("database retrieval via mapperDao") {
        val person = mapperDao.select(personEntity, 1).get
        person should equal(person1)
        person.toString should equal("Hancox, Peter")
        person.homeAddress.toString should equal("46 Dettmann Avenue, Longueville, NSW 2066, Australia")
        person.workAddress.toString should equal("PO Box 1383, Lane Cove, NSW 1595, Australia")
      }
      it("database persistence via CRUD DAO layer") {
        val inserted = personDao.create(person1)
        val selected = personDao.retrieve(inserted.id).get
        selected should equal(inserted)
        selected.toString should equal("Hancox, Peter")
        selected.homeAddress.toString should equal("46 Dettmann Avenue, Longueville, NSW 2066, Australia")
        selected.workAddress.toString should equal("PO Box 1383, Lane Cove, NSW 1595, Australia")
      }
      it("database updates via CRUD DAO layer") {
        val selected = personDao.retrieve(1).get
        selected.firstName = "Pedro"
        val updated = personDao.update(selected)
        val checkUpdated = personDao.retrieve(selected.id).get
        checkUpdated should equal(selected)
        checkUpdated.toString should equal("Hancox, Pedro")
      }
    }
    describe(s"should support ${jdbcDbManager} persistance via Hibernate including") {
      it("should support database persistence") {
        entityManager.getTransaction().begin()
        entityManager.persist(person1)
        entityManager.getTransaction().commit()
      }
      it("should support database retrieval") {
        entityManager.getTransaction().begin()
        val person = entityManager.find(classOf[Person], 2)
        entityManager.getTransaction().commit()
        person should equal(person1)
        person.toString should equal("Hancox, Peter")
        person.homeAddress.toString should equal("46 Dettmann Avenue, Longueville, NSW 2066, Australia")
        person.workAddress.toString should equal("PO Box 1383, Lane Cove, NSW 1595, Australia")
      }
    }
  }
}
