package com.dtc.deltasoft

import org.junit.runner.RunWith
import org.scalatest.junit._
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext

/**
 * Unit test suite for logging framework.
 *
 */
@RunWith(classOf[JUnitRunner])
class LoggingSpec extends FunSpec with ShouldMatchers with Logging {

  describe("The logging framework") {
    it("should log error level messages") {
      error("Error level message")
    }
    it("should log exception messages") {
      try {
        throw new Exception("Exception message")
      } catch {
        case e: Throwable => error(e.withStackTrace)
      }
    }
  }
}
