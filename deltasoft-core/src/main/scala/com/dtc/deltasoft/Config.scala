package com.dtc.deltasoft

import scala.collection.JavaConversions._
import java.io.File

import org.apache.commons.configuration._
import org.apache.commons.configuration.event._

/**
 * Listener for logging configuration events.
 *
 */
object ConfigurationListener extends ConfigurationListener with ConfigurationErrorListener with Logging {
  def configurationChanged(event: ConfigurationEvent) = {
    if (!event.isBeforeUpdate()) {
      val msg = s"ConfigurationEvent[${event.getType}]:" +
        { if (event.getPropertyName != null) s" PropertyName=${event.getPropertyName}" } +
        { if (event.getPropertyValue != null) s" PropertyValue=${event.getPropertyValue}" }
      info(msg)
    }
  }
  def configurationError(event: ConfigurationErrorEvent) = {
    val msg = s"ConfigurationErrorEvent[${event.getType}]"
    error(msg)
  }
}

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
      case e: ConfigurationException => {
        error(e.withStackTrace)
        val config = new CombinedConfiguration()
        config.addConfiguration(new SystemConfiguration)
        config.addConfiguration(new EnvironmentConfiguration)
        config
      }
    }
    config.addConfigurationListener(ConfigurationListener)
    config.addErrorListener(ConfigurationListener)
    def logConfig(key: String) = { info(key + "=" + config.getString(key)) }
    info("*" * 80)
    logConfig("DSTF_CONFIG")
    info("*" * 80)
    for {
      key <- config.getKeys()
    } logConfig(key)
    info("*" * 80)
    config
  }
}
