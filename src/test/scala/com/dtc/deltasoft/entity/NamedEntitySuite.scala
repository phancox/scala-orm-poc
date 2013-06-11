package com.dtc.deltasoft.entity

import scala.slick.driver.ExtendedProfile
import scala.slick.driver.{ PostgresDriver => _, _ }
import scala.slick.session.{ Database, Session }
import javax.persistence._
import java.util.Properties

import com.dtc.deltasoft.Config._
import org.apache.commons.configuration.PropertiesConfiguration

import org.junit.runner.RunWith
import org.scalatest.FunSpec
import org.scalatest.junit._
import org.scalatest.matchers.ShouldMatchers

/**
 * Unit test suite for the [[NamedEntity]] entity.
 *
 */
@RunWith(classOf[JUnitRunner])
class NamedEntitySpec extends FunSpec with ShouldMatchers {

  class DAL(override val profile: ExtendedProfile) extends NamedEntityProfile with Profile {}
  val jdbcDbManager = com.dtc.deltasoft.entity.jdbcDbManager
  implicit val dbConfig = DbConfig(2)
  val namedEntityEntity = new NamedEntityEntity
  val entities = List(namedEntityEntity)
  val ormConnections = getOrmConnections(entities)
  val slickDb = ormConnections.slickDb
  val dal = new DAL(ormConnections.slickDriver)
  import dal.profile.simple._
  val mapperDao = ormConnections.mapperDao
  val namedEntityDao = new NamedEntityDao(ormConnections)

  lazy val emf = Persistence.createEntityManagerFactory(jdbcDbManager)
  lazy val entityManager = emf.createEntityManager()

  var namedEntity1: NamedEntityImpl = _

  describe("A NamedEntity entity") {
    describe("should support being created") {
      it("using an empty constructor with properties set using JavaBean modifiers") {
        val namedEntity = NamedEntityImpl()
        namedEntity.setCode("ENTITYCODE")
        namedEntity.setName("Entity Name")
        namedEntity.setDescription("A brief description of the named entity.")
        namedEntity.setComments("Some comments associated with the named entity.")
        namedEntity should have(
          'code("ENTITYCODE"),
          'name("Entity Name"),
          'description("A brief description of the named entity."),
          'comments("Some comments associated with the named entity."))
        namedEntity.toString should equal("Entity Name (ENTITYCODE)")
        namedEntity1 = namedEntity
      }
      it("using a constructor with a named parameter list") {
        val namedEntity = NamedEntity(
          code = "ENTITYCODE",
          name = "Entity Name",
          description = "A brief description of the named entity.",
          comments = "Some comments associated with the named entity.")
        namedEntity.toString should equal("Entity Name (ENTITYCODE)")
      }
      it("using a constructor with positional parameters") {
        val namedEntity = NamedEntity("ENTITYCODE", "Entity Name",
          "A brief description of the named entity.",
          "Some comments associated with the named entity.")
        namedEntity.toString should equal("Entity Name (ENTITYCODE)")
      }
    }
    it("should support equality checks") {
      val namedEntity = NamedEntity()
      namedEntity.setCode("ENTITYCODE")
      namedEntity.setName("Entity Name")
      namedEntity.setDescription("A brief description of the named entity.")
      namedEntity.setComments("Some comments associated with the named entity.")
      namedEntity should equal(namedEntity1)
    }
    describe(s"should support ${jdbcDbManager} schema updates via Slick including") {
      it("table creation") {
        slickDb.withSession { implicit session: Session =>
          try {
            dal.NamedEntities.ddl.drop
          } catch {
            case _: Throwable =>
          }
          dal.NamedEntities.ddl.create
        }
      }
    }
    describe(s"should support ${jdbcDbManager} persistance via MapperDao including") {
      it("database persistence via mapperDao") {
        mapperDao.insert(namedEntityEntity, namedEntity1)
      }
      it("database retrieval via mapperDao") {
        val namedEntity = mapperDao.select(namedEntityEntity, 1).get
        namedEntity should equal(namedEntity1)
        namedEntity.toString should equal("Entity Name (ENTITYCODE)")
      }
      it("database persistence via CRUD DAO layer") {
        val inserted = namedEntityDao.create(namedEntity1)
        val selected = namedEntityDao.retrieve(inserted.id).get
        selected should equal(inserted)
        selected.toString should equal("Entity Name (ENTITYCODE)")
      }
      it("database updates via CRUD DAO layer") {
        val selected = namedEntityDao.retrieve(1).get
        selected.name = "Updated Name"
        val updated = namedEntityDao.update(selected)
        val checkUpdated = namedEntityDao.retrieve(selected.id).get
        checkUpdated should equal(selected)
        checkUpdated.toString should equal("Updated Name (ENTITYCODE)")
      }
    }
    describe(s"should support ${jdbcDbManager} persistance via Hibernate including") {
      it("should support database persistence") {
        entityManager.getTransaction().begin()
        entityManager.persist(namedEntity1)
        entityManager.getTransaction().commit()
      }
      it("should support database retrieval") {
        entityManager.getTransaction().begin()
        val namedEntity = entityManager.find(classOf[NamedEntityImpl], 2)
        entityManager.getTransaction().commit()
        namedEntity should equal(namedEntity1)
        namedEntity.toString should equal("Entity Name (ENTITYCODE)")
      }
    }
  }
}
