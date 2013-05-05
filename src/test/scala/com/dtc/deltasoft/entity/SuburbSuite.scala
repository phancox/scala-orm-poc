package com.dtc.deltasoft.entity

import org.junit.runner.RunWith
import org.scalatest.junit._
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers

import scala.slick.driver.ExtendedProfile
import scala.slick.session.{ Database, Session }

/**
 * Unit test suite for the [[Suburb]] entity.
 *
 */
@RunWith(classOf[JUnitRunner])
class SuburbSpec extends FunSpec with ShouldMatchers {

  lazy val db = Database.forURL("jdbc:log4jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", driver = "net.sf.log4jdbc.DriverSpy")

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
        suburb = Suburb(-1, "Longueville", "2066", "NSW", "Australia")
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
    describe("should support H2 database via Slick including") {
      import scala.slick.driver.H2Driver
      class DAL(override val profile: ExtendedProfile) extends SuburbEntity with Profile {}
      val dal = new DAL(H2Driver)
      import dal.profile.simple._
      it("schema creation") {
        db.withSession { implicit session: Session =>
          dal.Suburbs.ddl.create
        }
      }
    }
    describe("should support PostgreSQL database via Slick including") {
      import scala.slick.driver.PostgresDriver
      class DAL(override val profile: ExtendedProfile) extends SuburbEntity with Profile {}
      val dal = new DAL(PostgresDriver)
      import dal.profile.simple._
      it("schema creation") {
        db.withSession { implicit session: Session =>
          dal.Suburbs.ddl.drop
          dal.Suburbs.ddl.create
          dal.Suburbs.ddl.drop
        }
      }
    }
    describe("should support persistance via Hibernate including") {
      it("database persistence") {
        entityManager.getTransaction().begin()
        entityManager.persist(suburb)
        entityManager.getTransaction().commit()
      }
      it("database retrieval") {
        entityManager.getTransaction().begin()
        val suburb1 = entityManager.find(classOf[Suburb], -1)
        entityManager.getTransaction().commit()
        suburb1.toString should equal("Longueville, NSW 2066, Australia")
      }
    }
  }
}
