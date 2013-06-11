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
trait NamedEntityProfile { self: Profile =>
  import profile.simple._

  object NamedEntities extends Table[NamedEntity]("NAMED_ENTITY".asDbId) {

    def id = column[Int]("ID".asDbId, O.PrimaryKey, O.AutoInc)

    def code = column[String]("CODE".asDbId, O.NotNull)
    def name = column[String]("NAME".asDbId, O.NotNull)
    def description = column[String]("DESCRIPTION".asDbId, O.Nullable)
    def comments = column[String]("COMMENTS".asDbId, O.Nullable)

    def * = id ~ code ~ name ~ description ~ comments <> (
      { rs => new NamedEntity(rs._2, rs._3, rs._4, rs._5) with SurrogateIntId { val id: Int = rs._1 } },
      { namedEntity: NamedEntity => Some((0, "", "", "", "")) })

    def byId = createFinderBy(_.id)
  }
}

/**
 * MapperDao '''CRUD''' class for the [[NamedEntity]] entity.
 */
class NamedEntityDao(ormConnections: OrmConnections)(implicit dbConfig: DbConfig)
    extends TransactionalSurrogateIntIdCRUD[NamedEntity]
    with SurrogateIntIdAll[NamedEntity] {
  val mapperDao = ormConnections.mapperDao
  val queryDao = ormConnections.queryDao
  val txManager = ormConnections.txManager
  val entity = new NamedEntityEntity
}

/**
 * MapperDao '''Entity''' class for the [[NamedEntity]] entity.
 */
class NamedEntityEntity(implicit dbConfig: DbConfig)
    extends Entity[Int, SurrogateIntId, NamedEntity]("NAMED_ENTITY".asDbId) with Logging {
  trace("Creating NamedEntityEntity")
  val id = key("ID".asDbId) autogenerated (_.id)
  val code = column("CODE".asDbId) to (_.code)
  val name = column("NAME".asDbId) to (_.name)
  val description = column("DESCRIPTION".asDbId) to (_.description)
  val comments = column("COMMENTS".asDbId) to (_.comments)
  def constructor(implicit m: ValuesMap) =
    new NamedEntity(code, name, description, comments) with SurrogateIntId {
      val id: Int = NamedEntityEntity.this.id
    }
}

/**
 *
 */
object NamedEntity {

  def apply(code: String = null, name: String = null, description: String = null, comments: String = null) =
    new NamedEntity(code, name, description, comments)
}

/**
 * The NamedEntity entity ...
 *
 */
@MappedSuperclass
class NamedEntity() {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "ID") @BeanProperty
  var hibernateId: Int = _

  /**
   * The named entity's code.
   */
  @Column(name = "CODE") @BeanProperty var code: String = _

  /**
   * The named entity's name.
   */
  @Column(name = "NAME") @BeanProperty var name: String = _

  /**
   * The named entity's description.
   */
  @Column(name = "DESCRIPTION") @BeanProperty var description: String = _

  /**
   * The named entity's comments.
   */
  @Column(name = "COMMENTS") @BeanProperty var comments: String = _

  /*
   * Implementation of equals and hashCode based on Chapter 30 of "Programming in Scala".
   */

  def canEqual(other: Any) = other.isInstanceOf[NamedEntity]
  override def equals(other: Any) = other match {
    case that: NamedEntity => that.canEqual(this) &&
      this.code == that.code &&
      this.name == that.name &&
      this.description == that.description &&
      this.comments == that.comments
    case _ => false
  }

  override def hashCode() = {
    val prime = 41
    prime * (
      prime * (
        prime * (
          prime + code.hashCode
        ) + name.hashCode
      ) + description.hashCode
    ) + comments.hashCode
  }

  def this(code: String = null, name: String = null, description: String = null, comments: String = null) = {
    this()
    setCode(code)
    setName(name)
    setDescription(description)
    setComments(comments)
  }

  override def toString() = {
    name + " (" + code + ")"
  }
}
/**
 * The NamedEntityImpl entity ...
 *
 */
@javax.persistence.Entity
@Table(name = "NAMED_ENTITY")
case class NamedEntityImpl() extends NamedEntity
