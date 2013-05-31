package com.dtc.deltasoft.entity

import scala.slick.driver.ExtendedProfile
import scala.slick.driver.{ PostgresDriver => _, _ }
import scala.slick.session.{ Database, Session }
import javax.persistence._
import java.util.Properties
import com.googlecode.mapperdao.Query._
import com.googlecode.mapperdao.sqlfunction.SqlFunction
import com.googlecode.mapperdao.sqlfunction.StdSqlFunctions._

import org.junit.runner.RunWith
import org.scalatest.FunSpec
import org.scalatest.junit._
import org.scalatest.matchers.ShouldMatchers

/**
 * Unit test suite for the com.dtc.deltasoft.entity package.
 *
 */
@RunWith(classOf[JUnitRunner])
class PackageSpec extends FunSpec with ShouldMatchers {

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
  val queryDao = ormConnections.queryDao
  val suburbsDao = new SuburbsDao(ormConnections)

  describe("The entity package") {
    describe("dbid string interpolation") {
      it("should convert H2 identifiers to upper case") {
        "nAmE" asDbId (DbConfig("h2", 2)) should equal("NAME")
      }
      it("should convert PostgreSQL identifiers to lower case") {
        "nAmE" asDbId (DbConfig("postgresql", 2)) should equal("name")
      }
      it("should convert unkown identifiers to lower case") {
        "nAmE" asDbId (DbConfig("xyz", 2)) should equal("NAME")
      }
      it("should support implicit specification of database") {
        implicit val dbConfig = DbConfig("postgresql", 2)
        "nAmE".asDbId should equal("name")
      }
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
    it(s"should support populating the tables with mapperdao") {
      slickDb.withSession { implicit session: Session =>
        val s = suburbEntity
        mapperDao.insert(s, Suburb("Longueville", "2066", "NSW", "Australia"))
        mapperDao.insert(s, Suburb("AAAburb", "2001", "NSW"))
        mapperDao.insert(s, Suburb("AABburb", "2002", "NSW"))
        mapperDao.insert(s, Suburb("ABCburb", "2003", "NSW"))
      }
    }
    describe("mapperdao SqlFunction") {
      val s = suburbEntity
      it("should implement \"lower\" ") {
        val suburb = Suburb("Longueville", "2066", "NSW", "Australia")
        val suburbs1 = queryDao.query(select from s where (s.name === "longueville"))
        suburbs1 should equal(List())
        val suburbs2 = queryDao.query(select from s where (lower(s.name) === "longueville"))
        suburbs2 should equal(List(suburb))
        val suburbs3 = queryDao.query(select from s where (s.name like "long%"))
        suburbs3 should equal(List())
        val suburbs4 = queryDao.query(select from s where (lower(s.name) like "long%"))
        suburbs4 should equal(List(suburb))
        val suburbs5 = queryDao.query(select from s where (lower(s.name) like lower("LONG%")))
        suburbs5 should equal(List(suburb))
      }
    }
    describe("getSearchQuery") {
      val s = suburbEntity
      it("should handle an empty Column/Criteria list") {
        val query = getSearchQuery(s, List())
        val results = queryDao.query(query)
        results.length should equal(4)
      }
      it("should handle a Column/Criteria list with one pair") {
        val query = getSearchQuery(s, List((s.name, "Aa")))
        val results = queryDao.query(query)
        results.length should equal(2)
      }
      it("should handle a Column/Criteria list with two pairs") {
        val query = getSearchQuery(s, List((s.name, "Aab"),(s.postcode, "200")))
        val results = queryDao.query(query)
        results.length should equal(1)
      }
      it("should handle a null value parameter") {
        val query = getSearchQuery(s, List((s.name, null)))
        val results = queryDao.query(query)
        results.length should equal(4)
      }
      it("should handle an empty value parameter") {
        val query = getSearchQuery(s, List((s.name, "")))
        val results = queryDao.query(query)
        results.length should equal(4)
      }
      it("should handle two pairs where the first value is empty") {
        val query = getSearchQuery(s, List((s.name, ""),(s.postcode, "200")))
        val results = queryDao.query(query)
        results.length should equal(3)
      }
      it("should handle two pairs where the second value is empty") {
        val query = getSearchQuery(s, List((s.name, "Aab"),(s.postcode, "")))
        val results = queryDao.query(query)
        results.length should equal(1)
      }
      it("should handle three pairs where the second value is empty") {
        val query = getSearchQuery(s, List((s.name, "Aab"),(s.postcode, ""),(s.state, "VIC")))
        val results = queryDao.query(query)
        results.length should equal(0)
      }
    }
    describe("getSearchWhereClause") {
      it("should handle an empty Column/Criteria list") {
        getSearchWhereClause(List()) should equal("")
      }
      it("should handle a Column/Criteria list with one pair") {
        getSearchWhereClause(List(("column1", "value1"))) should
          equal("where lower(column1) like lower('value1%')")
      }
      it("should handle a Column/Criteria list with two pairs") {
        getSearchWhereClause(List(("column1", "value1"), ("column2", "value2"))) should
          equal("where lower(column1) like lower('value1%') and lower(column2) like lower('value2%')")
      }
      it("should handle a null value parameter") {
        getSearchWhereClause(List(("column1", null))) should equal("")
      }
      it("should handle an empty value parameter") {
        getSearchWhereClause(List(("column1", ""))) should equal("")
      }
      it("should handle a Some[String]") {
        getSearchWhereClause(List(("column1", Some("value1")))) should
          equal("where lower(column1) like lower('value1%')")
      }
      it("should handle two pairs where the first value is empty") {
        getSearchWhereClause(List(("column1", ""), ("column2", "value2"))) should
          equal("where lower(column2) like lower('value2%')")
      }
      it("should handle two pairs where the second value is empty") {
        getSearchWhereClause(List(("column1", "value1"), ("column2", ""))) should
          equal("where lower(column1) like lower('value1%')")
      }
      it("should handle three pairs where the second value is empty") {
        getSearchWhereClause(List(("column1", "value1"), ("column2", ""), ("column3", "value3"))) should
          equal("where lower(column1) like lower('value1%') and lower(column3) like lower('value3%')")
      }
    }
  }
}
