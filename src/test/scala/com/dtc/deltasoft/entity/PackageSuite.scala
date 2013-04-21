package com.dtc.deltasoft.entity

import javax.persistence.{ Entity, Persistence, Table }
import scala.collection.JavaConversions._
import org.junit.runner.RunWith
import org.scalatest.junit._
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers

object PackageSpec {

  // val emf = Persistence.createEntityManagerFactory("DeltaSoft")
  // val entityManager = emf.createEntityManager()
  // entityManager.close()
}

/**
 * Unit test suite for the com.dtc.deltasoft.entity package.
 *
 */
@RunWith(classOf[JUnitRunner])
class PackageSpec extends FunSpec with ShouldMatchers {

  describe("The entity package") {
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
    }
  }
}
