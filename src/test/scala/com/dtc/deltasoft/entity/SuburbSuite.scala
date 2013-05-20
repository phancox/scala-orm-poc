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
  implicit val dbms = dbmsName toLowerCase ()
  implicit val dataModelVersion = 2
  val suburbEntity = new SuburbEntity
  val entities = List(suburbEntity)
  val ormConnections = getOrmConnections(entities, dbms)
  val slickDb = ormConnections.slickDb
  val dal = new DAL(ormConnections.slickDriver)
  import dal.profile.simple._
  val mapperDao = ormConnections.mapperDao
  val suburbsDao = new SuburbsDao(ormConnections)

  lazy val emf = Persistence.createEntityManagerFactory(dbms)
  lazy val entityManager = emf.createEntityManager()

  var suburb: Suburb = _

  describe("A Suburb entity") {
    describe("should support being created") {
      it("using an empty constructor with properties set using JavaBean modifiers") {
        suburb = Suburb()
        suburb.setName("Longueville")
        suburb.setPostcode("2066")
        suburb.setState("NSW")
        suburb.setCountry("Australia")
        suburb.toString should equal("Longueville, NSW 2066, Australia")
        suburb should have(
          'name("Longueville"),
          'postcode("2066"),
          'state("NSW"),
          'country("Australia"))
      }
      it("using a constructor with a named parameter list") {
        suburb = Suburb(
          name = "Longueville",
          postcode = "2066",
          state = "NSW",
          country = "Australia")
        suburb.toString should equal("Longueville, NSW 2066, Australia")
      }
      it("using a constructor with positional parameters") {
        suburb = Suburb("Longueville", "2066", "NSW", "Australia")
        suburb.toString should equal("Longueville, NSW 2066, Australia")
      }
    }
    it("should support equality checks") {
      val suburb1 = Suburb()
      suburb1.setName("Longueville")
      suburb1.setPostcode("2066")
      suburb1.setState("NSW")
      suburb1.setCountry("Australia")
      suburb1 should equal(suburb)
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
        mapperDao.insert(suburbEntity, suburb)
      }
      it("database retrieval via mapperDao") {
        val suburb1 = mapperDao.select(suburbEntity, 1).get
        suburb1.toString should equal("Longueville, NSW 2066, Australia")
      }
      it("database persistence via CRUD DAO layer") {
        val inserted = suburbsDao.create(suburb)
        val selected = suburbsDao.retrieve(inserted.id).get
        selected should equal(inserted)
      }
    }
    describe(s"should support ${dbmsName} persistance via Hibernate including") {
      it("database persistence") {
        entityManager.getTransaction().begin()
        entityManager.persist(suburb)
        entityManager.getTransaction().commit()
      }
      it("database retrieval") {
        entityManager.getTransaction().begin()
        val suburb1 = entityManager.find(classOf[Suburb], 1)
        entityManager.getTransaction().commit()
        suburb1.toString should equal("Longueville, NSW 2066, Australia")
      }
    }
  }
}
