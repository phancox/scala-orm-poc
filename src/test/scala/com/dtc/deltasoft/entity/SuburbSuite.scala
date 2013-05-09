package com.dtc.deltasoft.entity

import org.junit.runner.RunWith
import org.scalatest.junit._
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import scala.slick.driver.ExtendedProfile
import scala.slick.session.{ Database, Session }
import java.util.Properties
import org.apache.tomcat.dbcp.dbcp.BasicDataSourceFactory
import com.googlecode.mapperdao.utils.Setup

/**
 * Unit test suite for the [[Suburb]] entity.
 *
 */
@RunWith(classOf[JUnitRunner])
class SuburbSpec extends FunSpec with ShouldMatchers {

  val connPropsH2 = new Properties
  connPropsH2.load(getClass.getResourceAsStream(s"/jdbc_h2.properties"))
  lazy val dsH2 = BasicDataSourceFactory.createDataSource(connPropsH2)
  lazy val dbH2 = Database.forDataSource(dsH2)

  val connPropsPostgresql = new Properties
  connPropsPostgresql.load(getClass.getResourceAsStream(s"/jdbc_postgresql.properties"))
  lazy val dsPostgresql = BasicDataSourceFactory.createDataSource(connPropsPostgresql)
  lazy val dbPostgresql = Database.forDataSource(dsPostgresql)

  private val entities = List(SuburbEntity)
  private val (jdbc, mapperDao, queryDao, txManager) =
    Setup(com.googlecode.mapperdao.utils.Database.byName("postgresql"), dsPostgresql, entities, None)

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
    describe("should support H2 database via Slick including") {
      import scala.slick.driver.H2Driver
      class DAL(override val profile: ExtendedProfile) extends SuburbProfile with Profile {}
      val dal = new DAL(H2Driver)
      import dal.profile.simple._
      it("schema creation") {
        dbH2.withSession { implicit session: Session =>
          dal.Suburbs.ddl.create
        }
      }
    }
    describe("should support PostgreSQL database via Slick including") {
      import scala.slick.driver.PostgresDriver
      class DAL(override val profile: ExtendedProfile) extends SuburbProfile with Profile {}
      val dal = new DAL(PostgresDriver)
      import dal.profile.simple._
      it("schema creation") {
        dbPostgresql.withSession { implicit session: Session =>
          dal.Suburbs.ddl.drop
          dal.Suburbs.ddl.create
        }
      }
    }
    describe("should support persistance via mapperdao including") {
      it("database persistence") {
        mapperDao.insert(SuburbEntity, suburb)
      }
      it("database retrieval") {
        val suburb1 = mapperDao.select(SuburbEntity, 1).get
        suburb1.toString should equal("Longueville, NSW 2066, Australia")
      }
    }
    ignore("should support persistance via Hibernate including") {
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
