package com.dtc.deltasoft.entity

import javax.persistence._
import scala.annotation.target.field
import scala.beans.BeanProperty

trait SuburbEntity { self: Profile =>
  import profile.simple._

  object Suburbs extends Table[Suburb]("SUBURB") {

    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

    def name = column[String]("NAME", O.NotNull)
    def postcode = column[String]("POSTCODE", O.NotNull)
    def state = column[String]("STATE", O.NotNull)
    def country = column[String]("COUNTRY", O.NotNull)

    def * = id ~ name ~ postcode ~ state ~ country <> (Suburb, Suburb.unapply _)
    def byId = createFinderBy(_.id)
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
 * The suburb's country (Defaults to "Australia"). To support Deltasoft framework version 1, this
 * field is tagged as transient so it won't be persisted in the database.
 *
 */
@Entity
@Table(name = "SUBURB")
case class Suburb(
    @(Id @field)@(GeneratedValue @field)@(Column @field)(name = "ID")@BeanProperty var id: Int = -1,
    @(Column @field)(name = "NAME")@BeanProperty var name: String = null,
    @(Column @field)(name = "POSTCODE")@BeanProperty var postcode: String = null,
    @(Column @field)(name = "STATE")@BeanProperty var state: String = null,
    @(Transient @field)@(Column @field)(name = "COUNTRY")@BeanProperty var country: String = "Australia") {

  override def toString() = {
    val statePostcode = List(state, postcode) filter (_ != null) mkString (" ") trim ()
    List(name, statePostcode, country) filter (x => x != null && x.toString.length != 0) mkString (", ")
  }
}
