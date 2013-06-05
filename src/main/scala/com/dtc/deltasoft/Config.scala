package com.dtc.deltasoft

import java.io.File

import org.apache.commons.configuration._

/**
 * Standard configuration management for DeltaSoft applications.
 *
 */
object Config extends Logging {

  private val defaultConfigurationBuilder = new DefaultConfigurationBuilder()
  defaultConfigurationBuilder.setFile(new File("config.xml"))

  /**
   * Returns a [[CombinedConfiguration]] configured as per the "config.xml" file located on the
   * class path.  If any exceptions are thrown while processing the configuration file, a default
   * [[CombinedConfiguration]] is returned that is initialised with values defined in the process
   * environment (i.e., environment variables) and the JVM system properties. 
   * 
   */
  lazy val config = {
    val config = try {
      defaultConfigurationBuilder.getConfiguration(true)
    } catch {
      case e: Throwable => {
        error(e.withStackTrace)
        val config = new CombinedConfiguration()
        config.addConfiguration(new SystemConfiguration)
        config.addConfiguration(new EnvironmentConfiguration)
        config
      }
    }
    def logConfig(key: String) = { info(key + "=" + config.getString(key)) }
    logConfig("DSTA_CONFIG")
    config
  }
}
