package com.dtc.deltasoft.entity

import scala.slick.driver.ExtendedProfile
import scala.slick.driver.{ PostgresDriver => _, _ }
import scala.slick.session.{ Database, Session }
import javax.persistence._
import java.util.Properties

import com.dtc.deltasoft.Config._
import org.apache.commons.configuration.PropertiesConfiguration

import org.junit.runner.RunWith
import org.scalatest.FunSpec
import org.scalatest.junit._
import org.scalatest.matchers.ShouldMatchers

/**
 * Unit test suite for the [[Address]] entity.
 *
 */
@RunWith(classOf[JUnitRunner])
class AddressSpec extends FunSpec with ShouldMatchers {

  class DAL(override val profile: ExtendedProfile) extends AddressProfile
    with SuburbProfile with Profile {}
  val jdbcDbManager = com.dtc.deltasoft.entity.jdbcDbManager
  implicit val dbConfig = DbConfig(2)
  val suburbEntity = new SuburbEntity
  val addressEntity = new AddressEntity
  val entities = List(suburbEntity, addressEntity)
  val ormConnections = getOrmConnections(entities)
  val slickDb = ormConnections.slickDb
  val dal = new DAL(ormConnections.slickDriver)
  import dal.profile.simple._
  val mapperDao = ormConnections.mapperDao
  val suburbsDao = new SuburbsDao(ormConnections)
  val addressesDao = new AddressesDao(ormConnections)

  lazy val emf = Persistence.createEntityManagerFactory(jdbcDbManager)
  lazy val entityManager = emf.createEntityManager()

  var address1: Address = _

  describe("An Address entity") {
    describe("should support being created") {
      it("using an empty constructor with properties set using JavaBean modifiers") {
        val address = Address()
        address.setStreet1("Hancox Residence")
        address.setSuburb(Suburb("Longueville", "2066", "NSW", "Australia"))
        address.setStreet2("46 Dettmann Avenue")
        address should have(
          'street1("Hancox Residence"),
          'suburb(Suburb("Longueville", "2066", "NSW", "Australia")),
          'street2("46 Dettmann Avenue"))
        address.toString should equal("Hancox Residence, 46 Dettmann Avenue, Longueville, NSW 2066, Australia")
        address1 = address
      }
      it("using a constructor with a named parameter list") {
        val address = Address(
          street1 = "Hancox Residence",
          street2 = "46 Dettmann Avenue",
          suburb = Suburb("Longueville", "2066", "NSW", "Australia"))
        address.toString should equal("Hancox Residence, 46 Dettmann Avenue, Longueville, NSW 2066, Australia")
      }
      it("using a constructor with positional parameters") {
        val address = Address("Hancox Residence", "46 Dettmann Avenue", Suburb("Longueville", "2066", "NSW", "Australia"))
        address.toString should equal("Hancox Residence, 46 Dettmann Avenue, Longueville, NSW 2066, Australia")
      }
    }
    it("should support equality checks") {
      val address = Address()
      address.setStreet1("Hancox Residence")
      address.setSuburb(Suburb("Longueville", "2066", "NSW", "Australia"))
      address.setStreet2("46 Dettmann Avenue")
      address should equal(address1)
    }
    describe(s"should support ${jdbcDbManager} schema updates via Slick including") {
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
    describe(s"should support ${jdbcDbManager} persistance via MapperDao including") {
      it("database persistence via mapperDao") {
        mapperDao.insert(addressEntity, address1)
      }
      it("database retrieval via mapperDao") {
        val address = mapperDao.select(addressEntity, 1).get
        address should equal(address1)
        address.toString should equal("Hancox Residence, 46 Dettmann Avenue, Longueville, NSW 2066, Australia")
      }
      it("database persistence via CRUD DAO layer") {
        val inserted = addressesDao.create(address1)
        val selected = addressesDao.retrieve(inserted.id).get
        selected should equal(inserted)
        selected.toString should equal("Hancox Residence, 46 Dettmann Avenue, Longueville, NSW 2066, Australia")
      }
      it("database updates via CRUD DAO layer") {
        val selected = addressesDao.retrieve(1).get
        selected.street2 = "42 New Street"
        val updated = addressesDao.update(selected)
        val checkUpdated = addressesDao.retrieve(selected.id).get
        checkUpdated should equal(selected)
        checkUpdated.toString should equal("Hancox Residence, 42 New Street, Longueville, NSW 2066, Australia")
      }
    }
    describe(s"should support ${jdbcDbManager} persistance via Hibernate including") {
      it("should support database persistence") {
        entityManager.getTransaction().begin()
        entityManager.persist(address1)
        entityManager.getTransaction().commit()
      }
      it("should support database retrieval") {
        entityManager.getTransaction().begin()
        val address = entityManager.find(classOf[Address], 2)
        entityManager.getTransaction().commit()
        address should equal(address1)
        address.toString should equal("Hancox Residence, 46 Dettmann Avenue, Longueville, NSW 2066, Australia")
      }
    }
  }
}
