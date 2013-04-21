package com.dtc.deltasoft

import java.util.Properties
import org.hibernate.cfg.Configuration
import org.hibernate.dialect.Dialect
import org.hibernate.ejb.Ejb3Configuration
import grizzled.slf4j.Logging

/**
 * DeltaSoft technical architecture framework support for JPA and a collection of core standard
 * entities such as [[Person]], [[Address]], ...
 */
package object entity extends Logging {
  trace("Creating package object.")

  val ejb3Configuration: Ejb3Configuration =
    new Ejb3Configuration().configure("DeltaSoft", new Properties())
  val hbmcfg: Configuration = ejb3Configuration.getHibernateConfiguration()
  val dialect = Dialect.getDialect(hbmcfg.getProperties())

  def getSchemaCreationScript = {
    val schemaCreationScript = hbmcfg.generateSchemaCreationScript(dialect)
    debug(schemaCreationScript mkString ("\n"))
    schemaCreationScript
  }

  def getSearchWhereClause(columnCriteriaList: List[(String, String)]): String = {
    def transform(columnValue: (String, String)): String = {
      s"lower(${columnValue._1}) like lower('${columnValue._2}%')"
    }
    def iterate(columnCriteriaList: List[(String, String)]): String = columnCriteriaList match {
      case x :: Nil => transform(x._1, x._2)
      case x :: xs => transform(x._1, x._2) + " and " + iterate(xs)
    }
    columnCriteriaList match {
      case List() => ""
      case _ => "where " + iterate(columnCriteriaList)
    }
  }
}
