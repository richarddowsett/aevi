package controllers

import javax.inject._

import dao.UserDao
import model.ValidUsernameRequest
import play.api.libs.json.Json
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class UserCreationController @Inject() (userDao: UserDao) extends Controller {

  /**
   * Return all users present in the datbase. This would form part of Admin functionality
   *
   * Returns a list of all Users
   */
  def allUsers = Action { implicit request =>
    NotImplemented(Json.obj("success" -> false))
  }

  def validUsername() = Action(BodyParsers.parse.json) { implicit request => {
    request.body.validate[ValidUsernameRequest].fold(e => {
      println(s"failed to parse request: $e") // change this to logging
      BadRequest(Json.obj("error" -> "parsing error", "success" -> false))
    }, r => {
      Ok(Json.obj("username" -> r.username, "isValid" -> userDao.isValidUsername(r.username)))
    })
  }
  }


}
