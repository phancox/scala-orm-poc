package com.dtc.deltasoft.entity

import javax.persistence.{ Entity, Persistence, Table }
import scala.collection.JavaConversions._
import org.junit.runner.RunWith
import org.scalatest.junit._
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers

object SuburbSpec {

  val emf = Persistence.createEntityManagerFactory("DeltaSoft")
  val entityManager = emf.createEntityManager()
  // entityManager.close()
}

/**
 * Unit test suite for the [[Suburb]] entity.
 *
 */
@RunWith(classOf[JUnitRunner])
class SuburbSpec extends FunSpec with ShouldMatchers {

  val entityManager = SuburbSpec.entityManager

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
      suburb1 should equal (suburb)
    }
    it("should support database persistence") {
      entityManager.getTransaction().begin()
      entityManager.persist(suburb)
      entityManager.getTransaction().commit()
    }
    it("should support database retrieval") {
      entityManager.getTransaction().begin()
      val suburb1 = entityManager.find(classOf[Suburb], 1)
      entityManager.getTransaction().commit()
      suburb1.toString should equal("Longueville, NSW 2066, Australia")
    }
  }
}
