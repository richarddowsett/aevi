package controllers

import javax.inject._

import dao.UserDao
import model.{AddUserPayload, ValidUsernameRequest}
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class UserCreationController @Inject()(userDao: UserDao) extends Controller {

  import scala.concurrent.ExecutionContext.Implicits.global

  def addUser() = Action.async(BodyParsers.parse.json) { implicit request => {
    request.body.validate[AddUserPayload].fold(e => {
      println(s"Failed to parse AddUserPayload: $e")
      Future.successful(BadRequest(Json.obj("error" -> "parsing error", "success" -> false)))
    }, r => {
      userDao.addUser(r.user).map(fut => {
        fut.fold(e => {
          println(s"Error while adding user: $e")
          BadRequest(Json.obj("error" -> e, "success" -> false))
        }, _ => Ok(Json.obj("username" -> r.user.username, "success" -> true)))
      })
    })
  }
  }

  /**
   * Return all users present in the datbase. This would form part of Admin functionality
   *
   * Returns a list of all Users
   */
  def allUsers = Action.async { implicit request =>
    userDao.getAllUsers().map(users => {
      Ok(Json.toJson(users))
    })
  }

  def validUsername() = Action.async(BodyParsers.parse.json) { implicit request => {
    request.body.validate[ValidUsernameRequest].fold(e => {
      println(s"failed to parse request: $e") // change this to logging
      Future.successful(BadRequest(Json.obj("error" -> "parsing error", "success" -> false)))
    }, r => {
      userDao.isValidUsername(r.username).map(isValid => {
        Ok(Json.obj("username" -> r.username, "isValid" -> isValid))
      })
    })
  }
  }


}
