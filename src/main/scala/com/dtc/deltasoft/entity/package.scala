package com.dtc.deltasoft

import java.util.Properties

import org.hibernate.cfg.Configuration
import org.hibernate.dialect.Dialect
import org.hibernate.ejb.Ejb3Configuration

import grizzled.slf4j.Logging

package object entity extends Logging {

  trace("Creating package object.")

  val ejb3Configuration: Ejb3Configuration =
    new Ejb3Configuration().configure("SportZman", new Properties())
  val hbmcfg: Configuration = ejb3Configuration.getHibernateConfiguration()
  val dialect = Dialect.getDialect(hbmcfg.getProperties())

  def getSchemaCreationScript = {
    val schemaCreationScript = hbmcfg.generateSchemaCreationScript(dialect)
    debug(schemaCreationScript mkString ("\n"))
    schemaCreationScript
  }
}
