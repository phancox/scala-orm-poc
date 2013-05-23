package com.dtc.deltasoft.entity

import org.junit.runner.RunWith
import org.scalatest.junit._
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import grizzled.slf4j.Logging

/**
 * Unit test suite for the com.dtc.deltasoft.entity package.
 *
 */
@RunWith(classOf[JUnitRunner])
class PackageSpec extends FunSpec with ShouldMatchers with Logging {

  describe("The entity package") {
    describe("dbid string interpolation") {
      it("should convert H2 identifiers to upper case") {
        "nAmE" asDbId(DbConfig("h2",2)) should equal("NAME") 
      }
      it("should convert PostgreSQL identifiers to lower case") {
        "nAmE" asDbId(DbConfig("postgresql",2)) should equal("name") 
      }
      it("should convert unkown identifiers to lower case") {
        "nAmE" asDbId(DbConfig("xyz",2)) should equal("NAME") 
      }
      it("should support implicit specification of database") {
        implicit val dbConfig = DbConfig("postgresql",2)
        "nAmE".asDbId should equal("name")  
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
        getSearchWhereClause(List(("column1", "value1"),("column2", "value2"))) should
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
        getSearchWhereClause(List(("column1", ""),("column2", "value2"))) should
          equal("where lower(column2) like lower('value2%')")
      }
      it("should handle two pairs where the second value is empty") {
        getSearchWhereClause(List(("column1", "value1"),("column2", ""))) should
          equal("where lower(column1) like lower('value1%')")
      }
      it("should handle three pairs where the second value is empty") {
        getSearchWhereClause(List(("column1", "value1"),("column2", ""),("column3", "value3"))) should
          equal("where lower(column1) like lower('value1%') and lower(column3) like lower('value3%')")
      }
    }
  }
}
