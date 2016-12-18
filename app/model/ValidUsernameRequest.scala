package model

import play.api.libs.functional.syntax._
import play.api.libs.json._


case class ValidUsernameRequest(username: String)

object ValidUsernameRequest {

  // http://stackoverflow.com/a/24436130/16759
  implicit val reads: Reads[ValidUsernameRequest] = (JsPath \ "username").read[String]
    .map { username => ValidUsernameRequest(username) }

  implicit val writes: Writes[ValidUsernameRequest] = (JsPath \ "username").write[String]
    .contramap { (payload: ValidUsernameRequest) => payload.username }

}
