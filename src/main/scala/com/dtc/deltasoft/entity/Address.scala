package com.dtc.deltasoft.entity

import javax.persistence._
import scala.beans.BeanProperty

/**
 *
 */
object Address {

  def apply(street1: String = null, suburb: Suburb = null, street2: String = null) =
    new Address(street1, suburb, street2)
}

/**
 *
 */
@Entity
@Table(name = "ADDRESS")
class Address() {

  @Id
  @GeneratedValue
  @Column(name = "ID")
  @BeanProperty
  var id: Int = _

  @Column(name = "STREET_1", length = 32)
  @BeanProperty
  var street1: String = null

  @Column(name = "STREET_2", length = 32)
  @BeanProperty
  var street2: String = null

  @OneToOne(cascade = Array(CascadeType.ALL))
  @JoinColumn(name = "SUBURB__ID")
  @BeanProperty
  var suburb: Suburb = null

  def this(street1: String = null, suburb: Suburb = null, street2: String = null) = {
    this()
    setStreet1(street1)
    setStreet2(street2)
    setSuburb(suburb)
  }

  /*
   * Implementation of hashCode and equals based on discussion in Chapter 30 of "Programming in Scala".
   */
  override def hashCode = super.hashCode

  override def equals(other: Any) = other match {
    case that: Address => (that canEqual this) &&
      (this.street1 == that.street1) &&
      (this.suburb == that.suburb) &&
      (this.street2 == that.street2)
    case _ => false
  }
  def canEqual(other: Any) = other.isInstanceOf[Address]

  override def toString() = {
    List(street1, street2, suburb) filter (_ != null) mkString(", ")
  }
}