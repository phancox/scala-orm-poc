package com.dtc.deltasoft.entity

import javax.persistence.{ Entity, Persistence, Table }
import scala.collection.JavaConversions._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit._
import grizzled.slf4j.Logging

object PersonSuite extends Logging {

  val emf = Persistence.createEntityManagerFactory("DeltaSoft")
  val entityManager = emf.createEntityManager()
  // entityManager.close()
}

/**
 *
 */
@RunWith(classOf[JUnitRunner])
class PersonSuite extends FunSuite with Logging {

  val entityManager = PersonSuite.entityManager

  var person: Person = _

  /**
   * Create an instance of Person using the primary constructor and set its properties using
   * JavaBean modifiers as required for Hibernate usage.
   */
  test("Create a Person using JavaBean properties.") {

    person = Person()
    person.setSurname("Hancox")
    person.setFirstName("Peter")
    person.setHomeAddress(Address("46 Dettmann Avenue", Suburb("Longueville", "2066", "NSW", "Australia")))
    person.setWorkAddress(Address("PO Box 1383", Suburb("Lane Cove", "1595", "NSW", "Australia")))
    expect("Hancox, Peter") { person.toString }
  }

  /**
   * Persist the Person using the Hibernate entity manager.
   */
  test("Persist Person using Hibernate entity manager.") {

    entityManager.getTransaction().begin()
    entityManager.persist(person)
    entityManager.getTransaction().commit()
  }

  /**
   * Retrieve the Person using the Hibernate entity manager and compare it with the original
   * Person instance.
   */
  test("Retrieve Person using Hibernate entity manager.") {

    entityManager.getTransaction().begin()
    val person1 = entityManager.find(classOf[Person], 1)
    entityManager.getTransaction().commit()
    expect("Hancox, Peter") { person1.toString }
    expect("46 Dettmann Avenue, Longueville, NSW 2066, Australia") { person1.homeAddress.toString }
    expect("PO Box 1383, Lane Cove, NSW 1595, Australia") { person1.workAddress.toString }
  }
}
