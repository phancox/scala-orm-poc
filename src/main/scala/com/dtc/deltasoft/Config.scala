package com.dtc.deltasoft

import java.io.File
import org.apache.commons.configuration._

/**
 * Standard configuration management for DeltaSoft applications.
 *
 */
object Config {

  private val defaultConfigurationBuilder = new DefaultConfigurationBuilder()
  defaultConfigurationBuilder.setFile(new File("config.xml"))
  lazy val config = defaultConfigurationBuilder.getConfiguration(true)
}
