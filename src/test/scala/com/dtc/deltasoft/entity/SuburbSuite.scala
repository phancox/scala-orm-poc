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
 * Unit test suite for the [[Suburb]] entity.
 *
 */
@RunWith(classOf[JUnitRunner])
class SuburbSpec extends FunSpec with ShouldMatchers {

  class DAL(override val profile: ExtendedProfile) extends SuburbProfile with Profile {}
  val dbmsName = "H2" // H2, PostgreSQL
  val dbms = dbmsName toLowerCase ()
  implicit val dbConfig = DbConfig(dbms, 2)
  val suburbEntity = new SuburbEntity
  val entities = List(suburbEntity)
  val ormConnections = getOrmConnections(s"jdbc_${dbms}", entities)
  val slickDb = ormConnections.slickDb
  val dal = new DAL(ormConnections.slickDriver)
  import dal.profile.simple._
  val mapperDao = ormConnections.mapperDao
  val suburbsDao = new SuburbsDao(ormConnections)

  lazy val emf = Persistence.createEntityManagerFactory(dbms)
  lazy val entityManager = emf.createEntityManager()

  var suburb1: Suburb = _

  describe("A Suburb entity") {
    describe("should support being created") {
      it("using an empty constructor with properties set using JavaBean modifiers") {
        val suburb = Suburb()
        suburb.setName("Longueville")
        suburb.setPostcode("2066")
        suburb.setState("NSW")
        suburb.setCountry("Australia")
        suburb should have(
          'name("Longueville"),
          'postcode("2066"),
          'state("NSW"),
          'country("Australia"))
        suburb.toString should equal("Longueville, NSW 2066, Australia")
        suburb1 = suburb
      }
      it("using a constructor with a named parameter list") {
        val suburb = Suburb(
          name = "Longueville",
          postcode = "2066",
          state = "NSW",
          country = "Australia")
        suburb.toString should equal("Longueville, NSW 2066, Australia")
      }
      it("using a constructor with positional parameters") {
        val suburb = Suburb("Longueville", "2066", "NSW", "Australia")
        suburb.toString should equal("Longueville, NSW 2066, Australia")
      }
    }
    it("should support equality checks") {
      val suburb = Suburb()
      suburb.setName("Longueville")
      suburb.setPostcode("2066")
      suburb.setState("NSW")
      suburb.setCountry("Australia")
      suburb should equal(suburb1)
    }
    describe(s"should support ${dbmsName} schema updates via Slick including") {
      it("table creation") {
        slickDb.withSession { implicit session: Session =>
          try {
            dal.Suburbs.ddl.drop
          } catch {
            case _: Throwable =>
          }
          dal.Suburbs.ddl.create
        }
      }
    }
    describe(s"should support ${dbmsName} persistance via MapperDao including") {
      it("database persistence via mapperDao") {
        mapperDao.insert(suburbEntity, suburb1)
      }
      it("database retrieval via mapperDao") {
        val suburb = mapperDao.select(suburbEntity, 1).get
        suburb should equal(suburb1)
        suburb.toString should equal("Longueville, NSW 2066, Australia")
      }
      it("database persistence via CRUD DAO layer") {
        val inserted = suburbsDao.create(suburb1)
        val selected = suburbsDao.retrieve(inserted.id).get
        selected should equal(inserted)
        selected.toString should equal("Longueville, NSW 2066, Australia")
      }
      it("database updates via CRUD DAO layer") {
        val selected = suburbsDao.retrieve(1).get
        selected.postcode = "0000"
        selected.country = "New Country"
        val updated = suburbsDao.update(selected)
        val checkUpdated = suburbsDao.retrieve(selected.id).get
        checkUpdated should equal(selected)
        checkUpdated.toString should equal("Longueville, NSW 0000, New Country")
      }
    }
    describe(s"should support ${dbmsName} persistance via Hibernate including") {
      it("database persistence") {
        entityManager.getTransaction().begin()
        entityManager.persist(suburb1)
        entityManager.getTransaction().commit()
      }
      it("database retrieval") {
        entityManager.getTransaction().begin()
        val suburb = entityManager.find(classOf[Suburb], 2)
        entityManager.getTransaction().commit()
        suburb should equal(suburb1)
        suburb.toString should equal("Longueville, NSW 2066, Australia")
      }
    }
  }
}
