package com.dtc.deltasoft.entity

import java.util.Properties
import javax.persistence._
import org.junit.runner.RunWith
import org.scalatest.junit._
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import scala.slick.driver.ExtendedProfile
import scala.slick.session.{ Database, Session }
import scala.slick.driver.{ PostgresDriver => _, _ }

/**
 * Unit test suite for the [[Address]] entity.
 *
 */
@RunWith(classOf[JUnitRunner])
class AddressSpec extends FunSpec with ShouldMatchers {

  class DAL(override val profile: ExtendedProfile) extends AddressProfile
    with SuburbProfile with Profile {}
  val dbmsName = "H2" // H2, PostgreSQL
  implicit val dbms = dbmsName toLowerCase ()
  val suburbEntity = new SuburbEntity
  val addressEntity = new AddressEntity
  val entities = List(suburbEntity, addressEntity)
  val ormConnections = getOrmConnections(entities, dbms)
  val slickDb = ormConnections.slickDb
  val dal = new DAL(ormConnections.slickDriver)
  import dal.profile.simple._
  val mapperDao = ormConnections.mapperDao
  lazy val emf = Persistence.createEntityManagerFactory(dbms)
  lazy val entityManager = emf.createEntityManager()

  var address: Address = _

  describe("An Address entity") {
    describe("should support being created") {
      it("using an empty constructor with properties set using JavaBean modifiers") {
        address = Address()
        address.setStreet1("Hancox Residence")
        address.setSuburb(Suburb("Longueville", "2066", "NSW", "Australia"))
        address.setStreet2("46 Dettmann Avenue")
        address.toString should equal("Hancox Residence, 46 Dettmann Avenue, Longueville, NSW 2066, Australia")
        address should have(
          'street1("Hancox Residence"),
          'suburb(Suburb("Longueville", "2066", "NSW", "Australia")),
          'street2("46 Dettmann Avenue"))
      }
      it("using a constructor with a named parameter list") {
        address = Address(
          street1 = "Hancox Residence",
          street2 = "46 Dettmann Avenue",
          suburb = Suburb("Longueville", "2066", "NSW", "Australia"))
        address.toString should equal("Hancox Residence, 46 Dettmann Avenue, Longueville, NSW 2066, Australia")
      }
      it("using a constructor with positional parameters") {
        address = Address("Hancox Residence", "46 Dettmann Avenue", Suburb("Longueville", "2066", "NSW", "Australia"))
        address.toString should equal("Hancox Residence, 46 Dettmann Avenue, Longueville, NSW 2066, Australia")
      }
    }
    it("should support equality checks") {
      val address1 = Address()
      address1.setStreet1("Hancox Residence")
      address1.setSuburb(Suburb("Longueville", "2066", "NSW", "Australia"))
      address1.setStreet2("46 Dettmann Avenue")
      address1 should equal(address)
    }
    describe(s"should support ${dbmsName} schema updates via Slick including") {
      it("table creation") {
        slickDb.withSession { implicit session: Session =>
          try {
            dal.Addresses.ddl.drop
            dal.Suburbs.ddl.drop
          } catch {
            case _: Throwable =>
          }
          dal.Suburbs.ddl.create
          dal.Addresses.ddl.create
        }
      }
    }
    describe(s"should support ${dbmsName} persistance via MapperDao including") {
      it("database persistence") {
        mapperDao.insert(addressEntity, address)
      }
      it("database retrieval") {
        val address1 = mapperDao.select(addressEntity, 1).get
        address1.toString should equal("Hancox Residence, 46 Dettmann Avenue, Longueville, NSW 2066, Australia")
      }
    }
    describe(s"should support ${dbmsName} persistance via Hibernate including") {
      it("should support database persistence") {
        entityManager.getTransaction().begin()
        entityManager.persist(address)
        entityManager.getTransaction().commit()
      }
      it("should support database retrieval") {
        entityManager.getTransaction().begin()
        val address1 = entityManager.find(classOf[Address], 1)
        entityManager.getTransaction().commit()
        address1.toString should equal("Hancox Residence, 46 Dettmann Avenue, Longueville, NSW 2066, Australia")
      }
    }
  }
}
