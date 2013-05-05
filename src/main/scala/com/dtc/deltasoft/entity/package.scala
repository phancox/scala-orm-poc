package com.dtc.deltasoft

import java.util.Properties
import javax.persistence.{ Entity, Persistence, Table }
import org.hibernate.cfg.Configuration
import org.hibernate.dialect.Dialect
import org.hibernate.ejb.Ejb3Configuration
import grizzled.slf4j.Logging
import scala.slick.driver.ExtendedProfile

/**
 * DeltaSoft technical architecture framework support for JPA and a collection of core standard
 * entities such as [[Person]], [[Address]], ...
 */
package object entity extends Logging {
  trace("Creating package object.")

  trait Profile {
    val profile: ExtendedProfile
  }

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

  /**
   * Returns a SQL "where" clause constructed from the supplied list of pairs where each pair is a
   * column name and a value to be matched.
   *
   * @param columnCriteriaList
   * A list of pairs where each pair is a column name and a value to be matched.
   *
   * @return
   * A SQL "where" clause constructed from the supplied list of pairs.
   *
   */
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
    val searchCriteriaList = columnCriteriaList map (transform _) filter (_.length > 0)
    searchCriteriaList match {
      case List() => ""
      case _ => searchCriteriaList mkString ("where ", " and ", "")
    }
  }
}
