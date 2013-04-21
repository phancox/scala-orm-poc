package com.dtc.deltasoft.entity

import javax.persistence._
import scala.beans.BeanProperty

/**
 *
 */
object Suburb {

  def apply(name: String = null, postcode: String = null, state: String = null, country: String = null) =
    new Suburb(name, postcode, state, country)
}

/**
 * The Suburb entity represents a location forming part of an [[Address]]. The following fields are
 * transient for compatibility with version 1 databases:
 *  - Country
 *
 */
@Entity
@Table(name = "SUBURB")
class Suburb() {

  @Id
  @GeneratedValue
  @Column(name = "ID")
  @BeanProperty
  var id: Int = _

  /**
   * The suburb's name.
   */
  @Column(name = "NAME", length = 40)
  @BeanProperty
  var name: String = null

  /**
   * The suburb's postcode.
   */
  @Column(name = "POSTCODE", length = 10)
  @BeanProperty
  var postcode: String = null

  /**
   * The suburb's state.
   */
  @Column(name = "STATE", length = 10)
  @BeanProperty
  var state: String = null

  /**
   * The suburb's country. This field is transient for compatibility with version 1 databases.
   */
  @Transient
  @Column(name = "COUNTRY", length = 32)
  @BeanProperty
  var country: String = "Australia"

  def this(name: String = null, postcode: String = null, state: String = null, country: String = null) = {
    this()
    setName(name)
    setPostcode(postcode)
    setState(state)
    setCountry(country)
  }

  /*
   * Implementation of equals and hashCode based on Chapter 30 of "Programming in Scala".
   */

  def canEqual(other: Any) = other.isInstanceOf[Suburb]
  override def equals(other: Any) = other match {
    case that: Suburb => that.canEqual(this) &&
      this.name == that.name &&
      this.postcode == that.postcode &&
      this.state == that.state &&
      this.country == that.country
    case _ => false
  }

  override def hashCode() = {
    val prime = 41
    prime * (
      prime * (
        prime * (
          prime + name.hashCode
        ) + postcode.hashCode
      ) + state.hashCode
    ) + country.hashCode
  }

  override def toString() = {
    val statePostcode = List(state, postcode) filter (_ != null) mkString (" ") trim ()
    List(name, statePostcode, country) filter (x => x != null && x.toString.length != 0) mkString (", ")
  }
}