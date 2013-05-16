package com.dtc.deltasoft.entity

import javax.persistence._
import scala.annotation.target.field
import scala.beans.BeanProperty
import com.googlecode.mapperdao._
import com.googlecode.mapperdao.{ Entity }
import scala.slick.driver._
import com.googlecode.mapperdao.utils.Setup
import grizzled.slf4j.Logging

/**
 * Persistence profile for Slick. Used for generating DDL.
 */
trait SuburbProfile { self: Profile =>
  import profile.simple._

  implicit val dbms = profile.asInstanceOf[BasicDriver] match {
    case H2Driver => "h2"
    case PostgresDriver => "postgresql"
  }

  object Suburbs extends Table[Suburb]("SUBURB".asDbId) {

    def id = column[Int]("ID".asDbId, O.PrimaryKey, O.AutoInc)

    def name = column[String]("NAME".asDbId, O.NotNull)
    def postcode = column[String]("POSTCODE".asDbId, O.NotNull)
    def state = column[String]("STATE".asDbId, O.NotNull)
    def country = column[String]("COUNTRY".asDbId, O.NotNull)

    class SuburbWithId extends Suburb with SurrogateIntId {
      val id: Int = 0
    }

    def * = id ~ name ~ postcode ~ state ~ country <> (
      { (i, n, p, s, c) => new SuburbWithId() },
      { s: Suburb => Some((0, "", "", "", "")) })

    def byId = createFinderBy(_.id)
  }
}

/**
 * Entity object for MapperDao.
 */
class SuburbEntity(implicit dbms: String) extends Entity[Int, SurrogateIntId, Suburb] with Logging {
  trace("Creating SuburbEntity")
  val id = key("ID".asDbId) autogenerated (_.id)
  val name = column("NAME".asDbId) to (_.name)
  val postcode = column("POSTCODE".asDbId) to (_.postcode)
  val state = column("STATE".asDbId) to (_.state)
  val country = column("COUNTRY".asDbId) to (_.country)
  def constructor(implicit m: ValuesMap) =
    new Suburb(name, postcode, state, country) with SurrogateIntId {
      val id: Int = SuburbEntity.this.id
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
 * The suburb's country (Defaults to "Australia"). To support DeltaSoft framework version 1, this
 * field is tagged as transient so it won't be persisted in the database.
 *
 */
@javax.persistence.Entity
@Table(name = "SUBURB")
case class Suburb(
    @(Column @field)(name = "NAME")@BeanProperty var name: String = null,
    @(Column @field)(name = "POSTCODE")@BeanProperty var postcode: String = null,
    @(Column @field)(name = "STATE")@BeanProperty var state: String = null,
    @(Transient @field)@(Column @field)(name = "COUNTRY")@BeanProperty var country: String = "Australia") {

  def this() = this(name = null)

  override def toString() = {
    val statePostcode = List(state, postcode) filter (_ != null) mkString (" ") trim ()
    List(name, statePostcode, country) filter (x => x != null && x.toString.length != 0) mkString (", ")
  }
}
