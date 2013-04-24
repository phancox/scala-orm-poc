package com.dtc.deltasoft

import java.util.Properties
import javax.persistence.{ Entity, Persistence, Table }
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

  lazy val emf = Persistence.createEntityManagerFactory("DeltaSoft")
  lazy val entityManager = emf.createEntityManager()

  def getSchemaCreationScript = {
    val ejb3Configuration: Ejb3Configuration =
      new Ejb3Configuration().configure("DeltaSoft", new Properties())
    val hbmcfg: Configuration = ejb3Configuration.getHibernateConfiguration()
    val dialect = Dialect.getDialect(hbmcfg.getProperties())
    val schemaCreationScript = hbmcfg.generateSchemaCreationScript(dialect)
    debug(schemaCreationScript mkString ("\n"))
    schemaCreationScript
  }

  implicit def wrapOptionalValue[T](value: T): Option[T] = Some(value)

  def getSearchWhereClause(columnCriteriaList: List[(String, Option[Any])]): String = {
    trace("getSearchWhereClause(columnCriteriaList=" + columnCriteriaList + ")")
    def transform(columnValue: (String, Option[Any])): String = {
      columnValue match {
        case (_, null) => ""
        case (_, None) => ""
        case (_, Some("")) => ""
        case _ => s"lower(${columnValue._1}) like lower('${columnValue._2.get}%')"
      }
    }
    def iterate(columnCriteriaList: List[(String, Option[Any])]): String = columnCriteriaList match {
      case x :: Nil => transform(x._1, x._2)
      case x :: xs => {
        val condition = transform(x._1, x._2)
        val remainder = iterate(xs)
        if (condition.length > 0 && remainder.length > 0)
          condition + " and " + remainder
        else if (remainder.length > 0)
          remainder
        else if (condition.length > 0)
          condition
        else
          ""
      }
    }
    columnCriteriaList match {
      case List() => ""
      case _ => {
        val searchWhereClause = iterate(columnCriteriaList)
        if (searchWhereClause.length == 0) ""
        else "where " + searchWhereClause
      }
    }
  }
}
