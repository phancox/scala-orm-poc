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

  /**
   * The person's surname.
   */
  @Column(name = "SURNAME", length = 32)
  @BeanProperty
  var surname: String = null

  /**
   * The person's first name.
   */
  @Column(name = "FIRST_NAME", length = 32)
  @BeanProperty
  var firstName: String = null

  /**
   * The person's home address.
   */
  @OneToOne(cascade = Array(CascadeType.ALL))
  @JoinColumn(name = "HOME_ADDRESS__ID")
  @BeanProperty
  var homeAddress: Address = null

  /**
   * The person's work address.
   */
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
   * Implementation of equals and hashCode based on Chapter 30 of "Programming in Scala".
   */

  def canEqual(other: Any) = other.isInstanceOf[Person]
  override def equals(other: Any) = other match {
    case that: Person => that.canEqual(this) &&
      this.surname == that.surname &&
      this.firstName == that.firstName
    case _ => false
  }

  override def hashCode() = {
    val prime = 41
    prime * (
      prime + surname.hashCode
    ) + firstName.hashCode
  }

  override def toString() = {
    surname + ", " + firstName
  }
}
