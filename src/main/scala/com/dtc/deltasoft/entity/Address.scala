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

  override def toString() = {
    List(street1, street2, suburb) filter (_ != null) mkString(", ")
  }
}