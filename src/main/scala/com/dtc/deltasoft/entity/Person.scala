package com.dtc.deltasoft.entity

import javax.persistence._
import scala.beans.BeanProperty

/**
 *
 */
object Person {

  def apply(surname: String = null, firstName: String = null,
            homeAddress: Address = null, workAddress: Address = null) =
    new Person(surname, firstName, homeAddress, workAddress)
}

/**
 *
 */
@Entity
@Table(name = "PERSON")
class Person() {

  @Id
  @GeneratedValue
  @Column(name = "ID")
  @BeanProperty
  var id: Int = _

  @Column(name = "SURNAME", length = 32)
  @BeanProperty
  var surname: String = null

  @Column(name = "FIRST_NAME", length = 32)
  @BeanProperty
  var firstName: String = null

  @OneToOne(cascade = Array(CascadeType.ALL))
  @JoinColumn(name = "HOME_ADDRESS__ID")
  @BeanProperty
  var homeAddress: Address = null

  @OneToOne(cascade = Array(CascadeType.ALL))
  @JoinColumn(name = "WORK_ADDRESS__ID")
  @BeanProperty
  var workAddress: Address = null

  def this(surname: String = null, firstName: String = null,
           homeAddress: Address = null, workAddress: Address = null) {
    this()
    setSurname(surname)
    setFirstName(firstName)
    setHomeAddress(homeAddress)
    setWorkAddress(workAddress)
  }

  /*
   * Implementation of hashCode and equals based on discussion in Chapter 30 of "Programming in Scala".
   */
  override def hashCode = super.hashCode

  override def equals(other: Any) = other match {
    case that: Person => (that canEqual this) &&
      (this.surname == that.surname) &&
      (this.firstName == that.firstName)
    case _ => false
  }
  def canEqual(other: Any) = other.isInstanceOf[Person]

  override def toString() = {
    surname + ", " + firstName
  }
}
