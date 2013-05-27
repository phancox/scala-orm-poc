package com.dtc.deltasoft.entity

import scala.annotation.target.field
import scala.beans.BeanProperty
import scala.slick.driver._
import javax.persistence._

import com.dtc.deltasoft.Logging
import com.googlecode.mapperdao._
import com.googlecode.mapperdao.{ Entity }
import com.googlecode.mapperdao.utils._

/**
 * Persistence profile for Slick. Used for generating DDL.
 */
trait SuburbProfile { self: Profile =>
  import profile.simple._

  object Suburbs extends Table[Suburb]("SUBURB".asDbId) {

    def id = column[Int]("ID".asDbId, O.PrimaryKey, O.AutoInc)

    def name = column[String]("NAME".asDbId, O.NotNull)
    def postcode = column[String]("POSTCODE".asDbId, O.NotNull)
    def state = column[String]("STATE".asDbId, O.NotNull)
    def country = column[String]("COUNTRY".asDbId, O.NotNull)

    def * = id ~ name ~ postcode ~ state ~ country <> (
      { rs => new Suburb(rs._2, rs._3, rs._4, rs._5) with SurrogateIntId { val id: Int = rs._1 } },
      { suburb: Suburb => Some((0, "", "", "", "")) })

    def byId = createFinderBy(_.id)
  }
}

/**
 * MapperDao '''CRUD''' class for the [[Suburb]] entity.
 */
class SuburbsDao(ormConnections: OrmConnections)(implicit dbConfig: DbConfig)
    extends TransactionalSurrogateIntIdCRUD[Suburb]
    with SurrogateIntIdAll[Suburb] {
  val mapperDao = ormConnections.mapperDao
  val queryDao = ormConnections.queryDao
  val txManager = ormConnections.txManager
  val entity = new SuburbEntity
}

/**
 * MapperDao '''Entity''' class for the [[Suburb]] entity.
 */
class SuburbEntity(implicit dbConfig: DbConfig)
    extends Entity[Int, SurrogateIntId, Suburb] with Logging {
  trace("Creating SuburbEntity")
  val id = key("ID".asDbId) autogenerated (_.id)
  val name = column("NAME".asDbId) to (_.name)
  val postcode = column("POSTCODE".asDbId) to (_.postcode)
  val state = column("STATE".asDbId) to (_.state)

  val country = if (dbConfig.dataModelVer == 2) column("COUNTRY".asDbId) to (_.country) else null

  def constructor(implicit m: ValuesMap) =
    if (dbConfig.dataModelVer == 1) {
      new Suburb(name, postcode, state) with SurrogateIntId {
        val id: Int = SuburbEntity.this.id
      }
    } else {
      new Suburb(name, postcode, state, country) with SurrogateIntId {
        val id: Int = SuburbEntity.this.id
      }
    }
}

/**
 * The Suburb entity represents a location forming part of an [[Address]]. The following fields are
 * transient for compatibility with version 1 databases:
 *  - Country
 *
 * @param id
 * The suburb's id (primary key).
 *
 * @param name
 * The suburb's name.
 *
 * @param postcode
 * The suburb's postcode.
 *
 * @param state
 * The suburb's state.
 *
 * @param country
 * The suburb's country (Defaults to "Australia").
 *
 * To support DeltaSoft framework version 1, this field would need to be tagged as transient so it
 * won't be persisted in the database.  e.g., @(Transient @field)
 *
 */
@javax.persistence.Entity
@Table(name = "SUBURB")
case class Suburb(
    @(Column @field)(name = "NAME")@BeanProperty var name: String = null,
    @(Column @field)(name = "POSTCODE")@BeanProperty var postcode: String = null,
    @(Column @field)(name = "STATE")@BeanProperty var state: String = null,
    @(Column @field)(name = "COUNTRY")@BeanProperty var country: String = "Australia") {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "ID") @BeanProperty
  var hibernateId: Int = _

  def this() = this(name = null)

  override def toString() = {
    val statePostcode = List(state, postcode) filter (_ != null) mkString (" ") trim ()
    List(name, statePostcode, country) filter (x => x != null && x.toString.length != 0) mkString (", ")
  }
}
