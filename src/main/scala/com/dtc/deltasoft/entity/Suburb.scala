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
 * The Suburb entity represents a location forming part of an [[Address]].
 */
@Entity
@Table(name = "SUBURB")
class Suburb() {

  @Id
  @GeneratedValue
  @Column(name = "ID")
  @BeanProperty
  var id: Int = _

  @Column(name = "NAME", length = 40)
  @BeanProperty
  var name: String = null

  @Column(name = "POSTCODE", length = 10)
  @BeanProperty
  var postcode: String = null

  @Column(name = "STATE", length = 10)
  @BeanProperty
  var state: String = null

  //  @Column(name = "COUNTRY", length = 32)
  @Transient // For compatibility with existing version 1 databases
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
   * Implementation of hashCode and equals based on discussion in Chapter 30 of "Programming in Scala".
   */
  override def hashCode = super.hashCode

  override def equals(other: Any) = other match {
    case that: Suburb => (that canEqual this) &&
      (this.name == that.name) &&
      (this.postcode == that.postcode) &&
      (this.state == that.state) &&
      (this.country == that.country)
    case _ => false
  }
  def canEqual(other: Any) = other.isInstanceOf[Suburb]

  override def toString() = {
    val statePostcode = List(state, postcode) filter (_ != null) mkString (" ") trim ()
    List(name, statePostcode, country) filter (x => x != null && x.toString.length != 0) mkString (", ")
  }
}