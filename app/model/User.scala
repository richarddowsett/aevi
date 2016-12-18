package model

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class User(username: String, email:String, forename:String, surname:String, telephone:String)

object User {

  implicit val reads:Reads[User] = (
    (JsPath \ "username").read[String] and
      (JsPath \ "email").read[String] and
      (JsPath \ "forename").read[String] and
      (JsPath \ "surname").read[String] and
      (JsPath \ "telephone").read[String]
    ).apply(User.apply _)

  implicit val writes:Writes[User] = (
    (JsPath \ "username").write[String] and
      (JsPath \ "email").write[String] and
      (JsPath \ "forename").write[String] and
      (JsPath \ "surname").write[String] and
      (JsPath \ "telephone").write[String]
    )(unlift(User.unapply))
}


