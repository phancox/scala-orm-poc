package com.dtc.deltasoft.entity

import javax.persistence._
import scala.beans.BeanProperty

@Entity
@Table(name = "PERSONX")
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
  }

  override def toString() = {
    "[" + id + "] " +
      surname + ", " +
      firstName
  }
}

