package modules

import com.google.inject.AbstractModule
import dao.{UserDao, UserDaoImpl}
import play.api.inject.ApplicationLifecycle
import play.api.{Configuration, Environment}
import slick.driver.H2Driver.api._
import slick.jdbc.JdbcBackend

import scala.concurrent.Future


class UserDaoModule(environment: Environment, configuration: Configuration) extends AbstractModule {
  override def configure(): Unit = {

    bind(classOf[UserDao]).to(classOf[UserDaoImpl])

  }
}
