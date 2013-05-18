package com.dtc.deltasoft.entity

import java.util.Properties
import org.junit.runner.RunWith
import org.scalatest.junit._
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import scala.slick.driver.ExtendedProfile
import scala.slick.session.{ Database, Session }
import scala.slick.driver.{ PostgresDriver => _, _ }

/**
 * Unit test suite for the [[Person]] entity.
 *
 */
@RunWith(classOf[JUnitRunner])
class PersonSpec extends FunSpec with ShouldMatchers {

  class DAL(override val profile: ExtendedProfile) extends PersonProfile
    with AddressProfile with SuburbProfile with Profile {}
  val dbmsName = "H2" // H2, PostgreSQL
  implicit val dbms = dbmsName toLowerCase ()
  val suburbEntity = new SuburbEntity
  val addressEntity = new AddressEntity
  val personEntity = new PersonEntity
  val entities = List(suburbEntity, addressEntity, personEntity)
  val ormConnections = getOrmConnections(entities, dbms)
  val slickDb = ormConnections.slickDb
  val dal = new DAL(ormConnections.slickDriver)
  import dal.profile.simple._
  val mapperDao = ormConnections.mapperDao

  var person: Person = _

  describe("A Person entity") {
    describe("should support being created") {
      it("using an empty constructor with properties set using JavaBean modifiers") {
        person = Person()
        person.setSurname("Hancox")
        person.setFirstName("Peter")
        person.setHomeAddress(Address("46 Dettmann Avenue", null, Suburb("Longueville", "2066", "NSW", "Australia")))
        person.setWorkAddress(Address("PO Box 1383", null, Suburb("Lane Cove", "1595", "NSW", "Australia")))
        person.toString should equal("Hancox, Peter")
        person should have(
          'surname("Hancox"),
          'firstName("Peter"),
          'homeAddress(Address("46 Dettmann Avenue", null, Suburb("Longueville", "2066", "NSW", "Australia"))),
          'workAddress(Address("PO Box 1383", null, Suburb("Lane Cove", "1595", "NSW", "Australia"))))
      }
      it("using a constructor with a named parameter list") {
        person = Person(
          surname = "Hancox",
          firstName = "Peter",
          homeAddress = Address("46 Dettmann Avenue", null, Suburb("Longueville", "2066", "NSW", "Australia")),
          workAddress = Address("PO Box 1383", null, Suburb("Lane Cove", "1595", "NSW", "Australia")))
        person.toString should equal("Hancox, Peter")
      }
      it("using a constructor with positional parameters") {
        person = Person("Hancox", "Peter",
          Address("46 Dettmann Avenue", null, Suburb("Longueville", "2066", "NSW", "Australia")),
          Address("PO Box 1383", null, Suburb("Lane Cove", "1595", "NSW", "Australia")))
        person.toString should equal("Hancox, Peter")
      }
    }
    it("should support equality checks") {
      val person1 = Person()
      person1.setSurname("Hancox")
      person1.setFirstName("Peter")
      person1.setHomeAddress(Address("46 Dettmann Avenue", null, Suburb("Longueville", "2066", "NSW", "Australia")))
      person1.setWorkAddress(Address("PO Box 1383", null, Suburb("Lane Cove", "1595", "NSW", "Australia")))
      person1 should equal(person)
    }
    describe(s"should support ${dbmsName} schema updates via Slick including") {
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
    describe(s"should support ${dbmsName} persistance via MapperDao including") {
      it("database persistence") {
        mapperDao.insert(personEntity, person)
      }
      it("database retrieval") {
        val person1 = mapperDao.select(personEntity, 1).get
        person1.toString should equal("Hancox, Peter")
        person1.homeAddress.toString should equal("46 Dettmann Avenue, Longueville, NSW 2066, Australia")
        person1.workAddress.toString should equal("PO Box 1383, Lane Cove, NSW 1595, Australia")
      }
    }
    ignore(s"should support ${dbmsName} persistance via Hibernate including") {
      it("should support database persistence") {
        entityManager.getTransaction().begin()
        entityManager.persist(person)
        entityManager.getTransaction().commit()
      }
      it("should support database retrieval") {
        entityManager.getTransaction().begin()
        val person1 = entityManager.find(classOf[Person], 1)
        entityManager.getTransaction().commit()
        person1.toString should equal("Hancox, Peter")
        person1.homeAddress.toString should equal("46 Dettmann Avenue, Longueville, NSW 2066, Australia")
        person1.workAddress.toString should equal("PO Box 1383, Lane Cove, NSW 1595, Australia")
      }
    }
  }
}
