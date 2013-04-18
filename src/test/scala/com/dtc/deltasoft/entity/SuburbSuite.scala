package com.dtc.deltasoft.entity

import javax.persistence.{ Entity, Persistence, Table }
import scala.collection.JavaConversions._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit._
import grizzled.slf4j.Logging

object SuburbSuite extends Logging {

  val emf = Persistence.createEntityManagerFactory("DeltaSoft")
  val entityManager = emf.createEntityManager()
  // entityManager.close()
}

/**
 *
 */
@RunWith(classOf[JUnitRunner])
class SuburbSuite extends FunSuite with Logging {

  val entityManager = SuburbSuite.entityManager

  var suburb: Suburb = _

  /**
   * Create an instance of Suburb using the primary constructor and set its properties using
   * JavaBean modifiers as required for Hibernate usage.
   */
  test("Create a Suburb using JavaBean properties.") {

    suburb = Suburb()
    suburb.setName("Longueville")
    suburb.setPostcode("2066")
    suburb.setState("NSW")
    suburb.setCountry("Australia")
    expect("Longueville, NSW 2066, Australia") { suburb.toString }
  }

  /**
   * Persist the Suburb using the Hibernate entity manager.
   */
  test("Persist Suburb using Hibernate entity manager.") {

    entityManager.getTransaction().begin()
    entityManager.persist(suburb)
    entityManager.getTransaction().commit()
  }

  /**
   * Retrieve the Suburb using the Hibernate entity manager and compare it with the original
   * Suburb instance.
   */
  test("Retrieve Suburb using Hibernate entity manager.") {

    entityManager.getTransaction().begin()
    val suburb1 = entityManager.find(classOf[Suburb], 1)
    entityManager.getTransaction().commit()
    expect("Longueville, NSW 2066, Australia") { suburb1.toString }
  }
}
