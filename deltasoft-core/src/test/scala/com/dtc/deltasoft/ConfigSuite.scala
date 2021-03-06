package com.dtc.deltasoft

import com.dtc.deltasoft.Config._
import org.apache.commons.configuration.ConfigurationConverter
import org.apache.commons.configuration.PropertiesConfiguration

import org.junit.runner.RunWith
import org.scalatest.junit._
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers

/**
 * Unit test suite for the configuration framework.
 *
 */
@RunWith(classOf[JUnitRunner])
class ConfigSpec extends FunSpec with ShouldMatchers {

  describe("The configuration framework") {
    it("should read JVM system properties") {
      config.getString("java.specification.name") should equal("Java Platform API Specification")
    }
    it("should read environment variables") {
      val classPath = config.getString("HOME")
      classPath should not be null
      info("HOME=" + classPath)
    }
    it("should load properties from test resources") {
      config.addConfiguration(new PropertiesConfiguration("config.properties"), "config", "config")
      val configProperties = ConfigurationConverter.getProperties(config.getConfiguration("config"))
      configProperties should not be null
      info(configProperties.toString)
      config.getString("config.testkey") should equal("testvalue")
    }
  }
}
