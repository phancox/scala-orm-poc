package com.dtc.deltasoft.entity

import org.junit.runner.RunWith
import org.scalatest.junit._
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import grizzled.slf4j.Logging


/**
 * Unit test suite for Spring framework integration.
 *
 */
@RunWith(classOf[JUnitRunner])
class SpringSpec extends FunSpec with ShouldMatchers with Logging {

  describe("The Spring framework") {
    describe("should support loading the application context") {
      it("from XML configuration file") {
        val ctx: ApplicationContext = new ClassPathXmlApplicationContext("Beans.xml");
        val suburb = ctx.getBean("suburbLongueville")
        suburb.toString should equal("Longueville, NSW 2066, Australia")
      }
    }
  }
}
