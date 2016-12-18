package model

import org.joda.time.DateTime
import play.api.libs.json.{Writes, JsPath, Reads}
import play.api.libs.functional.syntax._


case class AddUserPayload(user: User, timestamp: DateTime)

object AddUserPayload{

  implicit val reads: Reads[AddUserPayload] = (
    (JsPath \ "user").read[User] and
      (JsPath \ "timestamp").read[DateTime]
    ).apply(AddUserPayload.apply _)

}
