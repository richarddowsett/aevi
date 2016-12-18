package controllers

import javax.inject._

import dao.UserDao
import model.{AddUserPayload, ValidUsernameRequest}
import play.api.libs.json.Json
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class UserCreationController @Inject() (userDao: UserDao) extends Controller {

  def addUser() = Action(BodyParsers.parse.json) {implicit request => {
    request.body.validate[AddUserPayload].fold(e => {
      println(s"Failed to parse AddUserPayload: $e")
      BadRequest(Json.obj("error" -> "parsing error", "success" -> false))
    }, r => {
      userDao.addUser(r.user).fold(e => {
        println("Error while adding user: $e")
        BadRequest(Json.obj("error" -> e, "success" -> false))
      }, _ => Ok(Json.obj("username" -> r.user.username, "success" -> true)))
    })
  }}

  /**
   * Return all users present in the datbase. This would form part of Admin functionality
   *
   * Returns a list of all Users
   */
  def allUsers = Action { implicit request =>
    Ok(Json.toJson(userDao.getAllUsers()))
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
