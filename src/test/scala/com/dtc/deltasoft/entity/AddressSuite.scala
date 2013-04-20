package com.dtc.deltasoft.entity

import javax.persistence.{ Entity, Persistence, Table }
import scala.collection.JavaConversions._
import org.junit.runner.RunWith
import org.scalatest.junit._
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers

object AddressSpec {

  val emf = Persistence.createEntityManagerFactory("DeltaSoft")
  val entityManager = emf.createEntityManager()
  // entityManager.close()
}

/**
 * Unit test suite for the Address entity.
 *
 */
@RunWith(classOf[JUnitRunner])
class AddressSpec extends FunSpec with ShouldMatchers {

  val entityManager = AddressSpec.entityManager

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
          suburb = Suburb("Longueville", "2066", "NSW", "Australia"),
          street2 = "46 Dettmann Avenue")
        address.toString should equal("Hancox Residence, 46 Dettmann Avenue, Longueville, NSW 2066, Australia")
      }
      it("using a constructor with positional parameters") {
        address = Address("Hancox Residence", Suburb("Longueville", "2066", "NSW", "Australia"),
          "46 Dettmann Avenue")
        address.toString should equal("Hancox Residence, 46 Dettmann Avenue, Longueville, NSW 2066, Australia")
      }
    }
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
