package com.dtc.deltasoft.entity

import javax.persistence.{Entity, Persistence, Table}

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

  /**
   *
   */
  test("Create Person using primary constructor.") {

    val person: Person = Person("Smith", "Fred")
    val fullName = person.getFirstName + " " + person.getSurname();
    expect("Fred Smith") { fullName }
  }

  /**
   *
   */
  test("Create Person using primary constructor with named parameters.") {

    val person: Person = Person(firstName = "Fred", surname = "Smith")
    val fullName = person.getFirstName + " " + person.getSurname();
    expect("Fred Smith") { fullName }
  }

  /**
   *
   */
  test("Create Person using JavaBean properties.") {

    val person: Person = Person()
    person.setSurname("Smith")
    person.firstName = "Fred"
    val fullName = person.getFirstName + " " + person.getSurname();
    expect("Fred Smith") { fullName }
  }

  /**
   *
   */
  test("Create Person and persist in database.") {

    val entityManager = PersonSuite.entityManager
    entityManager.getTransaction().begin()

    var person = Person(
      "Hancox", "Peter",
      homeAddress = Address("46 Dettmann Avenue", "Longueville"))
    entityManager.persist(person)

    person.workAddress = Address("PO Box 1383", "Lane Cove", "NSW", "1595")
    entityManager.persist(person)

    person = new Person(
      "Hancox", "Angelina",
      homeAddress = Address("1 Test Street", "Longueville", country = "Australia"))
    entityManager.persist(person)

    entityManager.getTransaction().commit()
  }

  /**
   *
   */
  test("Extract Person records from database.") {

    val entityManager = PersonSuite.entityManager
    entityManager.getTransaction().begin()

    val lstEvent = entityManager.createQuery("from Person", classOf[Person]).getResultList()
    lstEvent foreach {person => info(person.toString)}

    entityManager.getTransaction().commit()
  }
}
