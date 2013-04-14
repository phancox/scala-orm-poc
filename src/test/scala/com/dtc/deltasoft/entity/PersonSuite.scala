package com.dtc.deltasoft.entity

import java.util.Properties

import javax.persistence.{ Entity, Persistence, Table }

import org.hibernate.cfg.Configuration
import org.hibernate.ejb.Ejb3Configuration
import org.hibernate.tool.hbm2ddl.SchemaExport

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit._

import grizzled.slf4j.Logging

object PersonSuite extends Logging {

  val emf = Persistence.createEntityManagerFactory("SportZman")
  val entityManager = emf.createEntityManager()
  // entityManager.close()

  val ejb3Configuration: Ejb3Configuration =
    new Ejb3Configuration().configure("SportZman", new Properties())
  val hbmcfg: Configuration = ejb3Configuration.getHibernateConfiguration()
  val schemaExport: SchemaExport = new SchemaExport(hbmcfg)

  schemaExport.setFormat(true)
  // schemaExport.execute(true, false, false, false)
}

/**
 *
 */
@RunWith(classOf[JUnitRunner])
class PersonSuite extends FunSuite {

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
}
