package com.dtc.deltasoft.entity

import scala.annotation.target.field
import scala.beans.BeanProperty
import scala.slick.driver._
import javax.persistence._

import com.dtc.deltasoft.Logging
import com.googlecode.mapperdao._
import com.googlecode.mapperdao.{ Entity }
import com.googlecode.mapperdao.utils._

/**
 * Persistence profile for Slick. Used for generating DDL.
 */
trait PersonProfile { self: AddressProfile with Profile =>
  import profile.simple._

  object Persons extends Table[Person]("PERSON".asDbId) {

    def id = column[Int]("ID".asDbId, O.PrimaryKey, O.AutoInc)

    def surname = column[String]("SURNAME".asDbId, O.NotNull)
    def firstName = column[String]("FIRST_NAME".asDbId)
    def homeAddressId = column[Int]("HOME_ADDRESS__ID".asDbId, O.NotNull)
    def workAddressId = column[Int]("WORK_ADDRESS__ID".asDbId, O.NotNull)

    def homeAddress = foreignKey("HOME_ADDRESS_FK", homeAddressId, Addresses)(_.id)
    def workAddress = foreignKey("WORK_ADDRESS_FK", workAddressId, Addresses)(_.id)

    def * = id ~ surname ~ firstName ~ homeAddressId ~ workAddressId <> (
      { rs => new Person(rs._2, rs._3, null, null) with SurrogateIntId { val id: Int = rs._1 } },
      { person: Person => Some((0, "", "", 0, 0)) })

    def byId = createFinderBy(_.id)
  }
}

/**
 * MapperDao '''CRUD''' class for the [[Person]] entity.
 */
class PersonsDao(ormConnections: OrmConnections)(implicit dbConfig: DbConfig)
    extends TransactionalSurrogateIntIdCRUD[Person]
    with SurrogateIntIdAll[Person] {
  val mapperDao = ormConnections.mapperDao
  val queryDao = ormConnections.queryDao
  val txManager = ormConnections.txManager
  val entity = new PersonEntity
}

/**
 * MapperDao '''Entity''' class for the [[Person]] entity.
 */
class PersonEntity(implicit dbConfig: DbConfig) extends Entity[Int, SurrogateIntId, Person] with Logging {
  trace("Creating PersonEntity")
  val id = key("ID".asDbId) autogenerated (_.id)
  val surname = column("SURNAME".asDbId) to (_.surname)
  val firstName = column("FIRST_NAME".asDbId) to (_.firstName)
  val homeAddress = manytoone(new AddressEntity) foreignkey ("HOME_ADDRESS__ID".asDbId) to (_.homeAddress)
  val workAddress = manytoone(new AddressEntity) foreignkey ("WORK_ADDRESS__ID".asDbId) to (_.workAddress)
  def constructor(implicit m: ValuesMap) =
    new Person(surname, firstName, homeAddress, workAddress) with SurrogateIntId {
      val id: Int = PersonEntity.this.id
    }
}

/**
 * The Person entity ...
 *
 * @param id
 * The persons' id (primary key).
 *
 * @param surname
 * The person's surname.
 *
 * @param firstName
 * The person's first name.
 *
 * @param homeAddress
 * The person's home address.
 *
 * @param workAddress
 * The person's work address.
 *
 */
@javax.persistence.Entity
@Table(name = "PERSON")
case class Person(
    @(Column @field)(name = "SURNAME")@BeanProperty var surname: String = null,
    @(Column @field)(name = "FIRST_NAME")@BeanProperty var firstName: String = null,
    @(OneToOne @field)(cascade = Array(CascadeType.ALL))@(JoinColumn @field)(name = "HOME_ADDRESS__ID")@BeanProperty var homeAddress: Address = null,
    @(OneToOne @field)(cascade = Array(CascadeType.ALL))@(JoinColumn @field)(name = "WORK_ADDRESS__ID")@BeanProperty var workAddress: Address = null) {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "ID") @BeanProperty
  var hibernateId: Int = _

  def this() = this(surname = null)

  override def toString() = {
    surname + ", " + firstName
  }
}
