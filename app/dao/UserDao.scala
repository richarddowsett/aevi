package dao

import com.google.inject.Inject
import model.User
import slick.jdbc.JdbcBackend
import slick.lifted.Tag
import slick.model.Table

import scalaz.{-\/, \/, \/-}

trait UserDao {
  /*
   * TODO: add proper error classes instead of String
   */
  def addUser(user: User): String \/ Unit

  def getAllUsers(): List[User]


  /**
   * Database error handling required.
   * @param username
   * @return
   */
  def isValidUsername(username: String): Boolean

}

class UserDaoImpl @Inject()() extends UserDao {

  var db = List.empty[User]

  override def addUser(user: User): \/[String, Unit] = {
    if(db.count(u => u.username == user.username) == 0){
      db = user +: db
      \/-(())
    }else {
      -\/("Duplicate user")
    }
  }

  override def isValidUsername(username: String): Boolean = {
    db.count(u => u.username == username) == 0
  }

  override def getAllUsers(): List[User] = {
    db
  }
}

