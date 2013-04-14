package com.dtc.deltasoft.entity

import javax.persistence._
import scala.beans.BeanProperty

@Entity
@Table(name = "ADDRESSX")
class Address() {

  @Id
  @GeneratedValue
  @Column(name = "ID")
  @BeanProperty
  var id: Int = _

  @Column(name = "STREET1", length = 32)
  @BeanProperty
  var street1: String = null

  @Column(name = "STREET2", length = 32)
  @BeanProperty
  var street2: String = null

  @Column(name = "SUBURB", length = 32)
  @BeanProperty
  var suburb: String = null

  @Column(name = "STATE", length = 32)
  @BeanProperty
  var state: String = null

  @Column(name = "POSTCODE", length = 8)
  @BeanProperty
  var postcode: String = null

  @Column(name = "COUNTRY", length = 32)
  @BeanProperty
  var country: String = null

  def this(street1: String = null, street2: String = null,
           suburb: String = null, state: String = null, postcode: String = null,
           country: String = null) {
    this()
    setStreet1(street1)
    setStreet2(street2)
  }

  override def toString() = {
    "[" + id + "] " +
      street1 + ", " +
      street2 + ", " +
      suburb + ", " +
      state + ", " +
      postcode + ", " +
      country
  }
}