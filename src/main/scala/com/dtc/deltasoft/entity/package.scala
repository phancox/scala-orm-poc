package com.dtc.deltasoft

import scala.annotation.target.field
import scala.beans.BeanProperty
import scala.slick.driver._
import javax.persistence._
import java.util.Properties

import com.dtc.deltasoft.Config._
import org.apache.commons.configuration.ConfigurationConverter
import com.googlecode.mapperdao._
import com.googlecode.mapperdao.jdbc._
import com.googlecode.mapperdao.utils.Setup
import org.apache.tomcat.dbcp.dbcp.BasicDataSourceFactory
import org.hibernate.cfg.Configuration
import org.hibernate.dialect.Dialect
import org.hibernate.ejb.Ejb3Configuration
import org.springframework.transaction.PlatformTransactionManager
import com.googlecode.mapperdao.Query._
import com.googlecode.mapperdao.sqlfunction.StdSqlFunctions._
import com.googlecode.mapperdao.schema.ColumnInfo
import com.googlecode.mapperdao.Entity

/**
 * DeltaSoft technical architecture framework support for JPA and a collection of core standard
 * entities such as [[Person]], [[Address]], ...
 */
package object entity extends Logging {
  trace("Creating package object.")

  def jdbcDbManager = {
    config.getString("jdbc.url", "") match {
      case e if e.contains(":h2:") => "H2"
      case e if e.contains(":postgresql:") => "PostgreSQL"
      case _ => null
    }
  }

  case class DbConfig(dataModelVer: Int)

  /**
   * Helper class to automatically convert database identifier names to the appropriate case based
   * on the target database.
   */
  implicit class DbIdHelper(val s: String) extends AnyVal {
    def asDbId = {
      jdbcDbManager match {
        case "PostgreSQL" => s.toLowerCase()
        case _ => s.toUpperCase()
      }
    }
  }

  trait Profile {
    val profile: ExtendedProfile

    implicit val dbConfig = DbConfig(2)
  }

  /**
   * Extends the Slick PostgreSQL driver to force identifier names to lower case by overriding the
   * ''quoteIdentifier'' function.
   *
   */
  object PostgresDriver extends scala.slick.driver.PostgresDriver {
    override def quoteIdentifier(identifier: String) = super.quoteIdentifier(identifier.toLowerCase)
  }

  case class OrmConnections(
    slickDriver: scala.slick.driver.ExtendedDriver, slickDb: scala.slick.session.Database,
    jdbc: Jdbc, mapperDao: MapperDao, queryDao: QueryDao, txManager: PlatformTransactionManager)

  /**
   * Returns a tuple of connection objects for interacting with the ORM persistence layer.
   *
   * @param entities
   * A list of entities to be managed by mapperdao.
   *
   * @param dbConfig
   * A tuple specifying the dbms to be used and the data model version, passed as implicit parameters.
   *
   * @return
   * A tuple of connection objects for interacting with the ORM persistence layer.
   *
   */
  def getOrmConnections(entities: List[Entity[_, _, _]]): OrmConnections = {

    val slickDriver = jdbcDbManager match {
      case "H2" => H2Driver
      case "PostgreSQL" => PostgresDriver
      case _ => H2Driver
    }

    lazy val jdbcConfig = config.configurationAt("jdbc")
    lazy val jdbcProperties = ConfigurationConverter.getProperties(jdbcConfig)
    def jdbcPropCopy = { val p = new Properties(); p.putAll(jdbcProperties); p.remove("password"); p }
    info("jdbcProperties=" + jdbcPropCopy)

    lazy val dataSource = BasicDataSourceFactory.createDataSource(jdbcProperties)
    lazy val slickDb = scala.slick.session.Database.forDataSource(dataSource)

    lazy val (jdbc, mapperDao, queryDao, txManager) =
      Setup(com.googlecode.mapperdao.utils.Database.byName(jdbcDbManager toLowerCase), dataSource, entities)

    OrmConnections(slickDriver, slickDb, jdbc, mapperDao, queryDao, txManager)
  }

  def getSchemaCreationScript = {
    val ejb3Configuration: Ejb3Configuration =
      new Ejb3Configuration().configure("DeltaSoft", new Properties())
    val hbmcfg: Configuration = ejb3Configuration.getHibernateConfiguration()
    val dialect = Dialect.getDialect(hbmcfg.getProperties())
    val schemaCreationScript = hbmcfg.generateSchemaCreationScript(dialect)
    debug(schemaCreationScript mkString ("\n"))
    schemaCreationScript
  }

  implicit def convertToOption[T](value: T): Option[T] = Some(value)

  type CriteriaListElement[T] = (ColumnInfo[T, String], String)
  def getSearchQuery[ID, T](entity: Entity[ID, SurrogateIntId, T], criteriaList: List[CriteriaListElement[T]]) = {
    val selectClause = select from entity
    val filteredCriteria = criteriaList filter (e => e._2 != null && e._2.length > 0)
    if (filteredCriteria.length > 0) {
      def criteriaExpression(e: CriteriaListElement[T]) = { (lower(e._1) like s"${e._2.trim.toLowerCase}%") }
      val whereClause = selectClause where criteriaExpression(filteredCriteria(0))
      filteredCriteria drop (1) foreach (criteria => { whereClause and criteriaExpression(criteria) })
    }
    selectClause
  }

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
