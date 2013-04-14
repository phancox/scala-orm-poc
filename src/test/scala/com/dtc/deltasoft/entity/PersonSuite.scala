package com.dtc.deltasoft.entity

import javax.persistence._

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit._

/**
 *
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {

  val emf = Persistence.createEntityManagerFactory("SportZman")
  val entityManager = emf.createEntityManager()
//  entityManager.close()

  /**
   *
   */
  test("Create Person using primary constructor.") {

    val person: Person = new Person("Smith", "Fred")
    val fullName = person.getFirstName + " " + person.getSurname();
    expect("Fred Smith") { fullName }
  }

  /**
   *
   */
  test("Create Person using primary constructor with named parameters.") {

    val person: Person = new Person(firstName = "Fred", surname = "Smith")
    val fullName = person.getFirstName + " " + person.getSurname();
    expect("Fred Smith") { fullName }
  }

  /**
   *
   */
  test("Create Person using JavaBean properties.") {

    val person: Person = new Person()
    person.setSurname("Smith")
    person.firstName = "Fred"
    val fullName = person.getFirstName + " " + person.getSurname();
    expect("Fred Smith") { fullName }
  }

  /**
   *
   */
  test("Create Person and persist in database.") {

    entityManager.getTransaction().begin()
 
    val person = new Person(
      "Hancox", "Peter",
      homeAddress = new Address("46 Dettmann Avenue", "Longueville"))
    entityManager.persist(person)

    entityManager.getTransaction().commit()
  }
}
