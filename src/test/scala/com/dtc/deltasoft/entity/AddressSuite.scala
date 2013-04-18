package com.dtc.deltasoft.entity

import javax.persistence.{ Entity, Persistence, Table }
import scala.collection.JavaConversions._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit._
import grizzled.slf4j.Logging

object AddressSuite extends Logging {

  val emf = Persistence.createEntityManagerFactory("DeltaSoft")
  val entityManager = emf.createEntityManager()
  // entityManager.close()
}

/**
 *
 */
@RunWith(classOf[JUnitRunner])
class AddressSuite extends FunSuite with Logging {

  val entityManager = AddressSuite.entityManager

  var address: Address = _

  /**
   * Create an instance of Address using the primary constructor and set its properties using
   * JavaBean modifiers as required for Hibernate usage.
   */
  test("Create an Address using JavaBean properties.") {

    address = Address()
    address.setStreet1("Hancox Residence")
    address.setStreet2("46 Dettmann Avenue")
    address.setSuburb(Suburb("Longueville", "2066", "NSW", "Australia"))
    expect("Hancox Residence, 46 Dettmann Avenue, Longueville, NSW 2066, Australia") { address.toString }
  }

  /**
   * Create instances of Address using alternate constructor with positional parameters.
   */
  test("Create Address instances using alternate constructor.") {

	var address = Address()
    
	// Omit optional property: Street2 
    address = Address("46 Dettmann Avenue", Suburb("Longueville", "2066", "NSW", "Australia"))
    expect("46 Dettmann Avenue, Longueville, NSW 2066, Australia") { address.toString }
  }

  /**
   * Persist the Address using the Hibernate entity manager.
   */
  test("Persist Address using Hibernate entity manager.") {

    entityManager.getTransaction().begin()
    entityManager.persist(address)
    entityManager.getTransaction().commit()
  }

  /**
   * Retrieve the Address using the Hibernate entity manager and compare it with the original
   * Address instance.
   */
  test("Retrieve Address using Hibernate entity manager.") {

    entityManager.getTransaction().begin()
    val address1 = entityManager.find(classOf[Address], 1)
    entityManager.getTransaction().commit()
    expect("Hancox Residence, 46 Dettmann Avenue, Longueville, NSW 2066, Australia") { address1.toString }
  }
}
