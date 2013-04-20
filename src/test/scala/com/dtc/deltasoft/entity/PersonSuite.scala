package com.dtc.deltasoft.entity

import javax.persistence.{ Entity, Persistence, Table }
import scala.collection.JavaConversions._
import org.junit.runner.RunWith
import org.scalatest.FunSpec
import org.scalatest.junit._
import grizzled.slf4j.Logging

object PersonSpec {

  val emf = Persistence.createEntityManagerFactory("DeltaSoft")
  val entityManager = emf.createEntityManager()
  // entityManager.close()
}

/**
 * Unit test suite for the Person entity.
 *
 */
@RunWith(classOf[JUnitRunner])
class PersonSpec extends FunSpec {

  val entityManager = PersonSpec.entityManager

  var person: Person = _

  describe("A Person entity") {
    describe("should support being created") {
      it("using an empty constructor with properties set using JavaBean modifiers") {
        person = Person()
        person.setSurname("Hancox")
        person.setFirstName("Peter")
        person.setHomeAddress(Address("46 Dettmann Avenue", Suburb("Longueville", "2066", "NSW", "Australia")))
        person.setWorkAddress(Address("PO Box 1383", Suburb("Lane Cove", "1595", "NSW", "Australia")))
        expect("Hancox, Peter") { person.toString }
      }
      it("using a constructor with a named parameter list") {
        person = Person(
          surname = "Hancox",
          firstName = "Peter",
          homeAddress = Address("46 Dettmann Avenue", Suburb("Longueville", "2066", "NSW", "Australia")),
          workAddress = Address("PO Box 1383", Suburb("Lane Cove", "1595", "NSW", "Australia")))
        expect("Hancox, Peter") { person.toString }
      }
      it("using a constructor with positional parameters") {
        person = Person("Hancox", "Peter",
          Address("46 Dettmann Avenue", Suburb("Longueville", "2066", "NSW", "Australia")),
          Address("PO Box 1383", Suburb("Lane Cove", "1595", "NSW", "Australia")))
        expect("Hancox, Peter") { person.toString }
      }
    }
    it("should support database persistence") {
      entityManager.getTransaction().begin()
      entityManager.persist(person)
      entityManager.getTransaction().commit()
    }
    it("should support database retrieval") {
      entityManager.getTransaction().begin()
      val person1 = entityManager.find(classOf[Person], 1)
      entityManager.getTransaction().commit()
      expect("Hancox, Peter") { person1.toString }
      expect("46 Dettmann Avenue, Longueville, NSW 2066, Australia") { person1.homeAddress.toString }
      expect("PO Box 1383, Lane Cove, NSW 1595, Australia") { person1.workAddress.toString }
    }
  }
}
