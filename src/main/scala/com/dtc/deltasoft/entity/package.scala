package com.dtc.deltasoft

import scala.annotation.target.field
import scala.beans.BeanProperty
import scala.slick.driver._
import javax.persistence._
import java.util.Properties

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

  case class DbConfig(dbms: String, dataModelVer: Int)

  /**
   * Helper class to automatically convert database identifier names to the appropriate case based
   * on the target database.
   */
  implicit class DbIdHelper(val s: String) extends AnyVal {
    def asDbId(implicit dbConfig: DbConfig) = {
      //debug(s"dbms = ${dbConfig.dbms}")
      dbConfig.dbms match {
        case "postgresql" => s.toLowerCase()
        case _ => s.toUpperCase()
      }
    }
  }

  trait Profile {
    val profile: ExtendedProfile

    val dbms = profile.asInstanceOf[BasicDriver] match {
      case H2Driver => "h2"
      case PostgresDriver => "postgresql"
    }

    implicit val dbConfig = DbConfig(dbms, 2)
  }

  /**
   * Extends the Slick PostgreSQL driver to force identifier names to lowercase by overriding the
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
   * @param dbPropFile
   * Name of the properties file specifying the database properties.  e.g, if dbPropFile == "db"
   * then the file "/db.properties" will be used.
   *
   * @param dbms
   * A '''String''' representing the DBMS. Possible values include:
   *  - h2
   *  - postgresql
   *
   *  @return
   *
   */
  def getOrmConnections(dbPropFile: String, entities: List[Entity[_, _, _]])(implicit dbConfig: DbConfig): OrmConnections = {

    val dbms = dbConfig.dbms

    val slickDriver = dbms match {
      case "h2" => H2Driver
      case "postgresql" => PostgresDriver
      case _ => H2Driver
    }

    val properties = new Properties
    properties.load(getClass.getResourceAsStream(s"/${dbPropFile}.properties"))
    lazy val dataSource = BasicDataSourceFactory.createDataSource(properties)
    lazy val slickDb = scala.slick.session.Database.forDataSource(dataSource)

    lazy val (jdbc, mapperDao, queryDao, txManager) =
      Setup(com.googlecode.mapperdao.utils.Database.byName(dbms), dataSource, entities)

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

  def getSearchQuery[ID, T](entity: Entity[ID, Persisted, T], criteriaList: List[(ColumnInfo[T, String], String)]) = {

    val filteredCriteria = criteriaList filter (_._2.length > 0)
    val selectClause = select from entity
    val test = filteredCriteria.length match {
      case 0 => new Where(selectClause)
      case 1 => selectClause where (lower(filteredCriteria(0)._1) like s"${filteredCriteria(0)._2.toLowerCase}%")
      case _ => selectClause where (lower(filteredCriteria(0)._1) like s"${filteredCriteria(0)._2.toLowerCase}%")
    }

    test
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
