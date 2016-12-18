package dao

import com.google.inject.Inject
import model.User
import slick.driver.H2Driver.api._

import scala.concurrent.Future
import scala.util.{Failure, Success}
import scalaz.{-\/, \/, \/-}

trait UserDao {
  /*
   * TODO: add proper error classes instead of String
   */
  def addUser(user: User): Future[String \/ Unit]

  def getAllUsers(): Future[Seq[User]]


  /**
   * Database error handling required.
   * @param username
   * @return
   */
  def isValidUsername(username: String): Future[Boolean]

}

class UserDaoImpl @Inject()(db: Database) extends UserDao {
  import scala.concurrent.ExecutionContext.Implicits.global

  val users = TableQuery[Users]

  override def addUser(user: User): Future[String \/ Unit] = {
       val insert =  DBIO.seq(
       users += user
    ).asTry
    db.run(insert).map({
      case Success(u) => \/-(())
      case Failure(f) => println(s"Error while inserting user: $f")
        -\/("insertion error")
    })
  }

  override def isValidUsername(username: String): Future[Boolean] = {
    val request = users.withFilter(u => u.username === username).countDistinct.result
    db.run(request).map(c => c == 0)
  }

  override def getAllUsers(): Future[Seq[User]] = {
    db.run(users.result)
  }
}

class Users(tag: Tag) extends Table[User](tag, "USERS") {
  def username = column[String]("USERNAME")

  def email = column[String]("EMAIL")

  def forename = column[String]("FORENAME")

  def surname = column[String]("SURNAME")

  def telephone = column[String]("TELEPHONE")

  def * = (username, email, forename, surname, telephone) <> ((User.apply _).tupled, User.unapply)
}

