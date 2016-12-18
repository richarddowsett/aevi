package controllers

import model.User
import org.joda.time.DateTime
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneAppPerTest, PlaySpec}
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers._

/**
 * using the database
 */
class UserCreationControllerIntegrationSpec extends PlaySpec with OneAppPerTest with MockitoSugar {

  "return an error when duplicate user is added" in {
    val inputUser = User("sample", "sample@email.com", "Sample", "User", "123456799")
    val request = FakeRequest(PUT, "/user").withHeaders("Host" -> "localhost").withBody(Json.obj("user" -> Json.toJson(inputUser), "timestamp" -> Json.toJson(new DateTime())))
    val addUser = route(app, request).get
    status(addUser) mustBe OK
    (contentAsJson(addUser) \ "success").as[Boolean] mustBe true

    val duplicate = route(app, request).get
    status(duplicate) must not be OK
    (contentAsJson(duplicate) \ "success").as[Boolean] mustBe false
  }

  "return 400 when parsing fails" in {
    val inputUser = User("sample", "sample@email.com", "Sample", "User", "123456799")
    val request = FakeRequest(PUT, "/user").withHeaders("Host" -> "localhost").withBody(Json.toJson(inputUser))
    val addUser = route(app, request).get
    status(addUser) mustBe BAD_REQUEST
    (contentAsJson(addUser) \ "error").as[String] mustEqual "parsing error"
  }

  "show username as invalid once added" in {
    val validUserPayload = FakeRequest(GET, "/username").withHeaders("Host" -> "localhost").withBody(Json.obj("username" -> "sampleTest"))
    val validUser = route(app, validUserPayload).get
    status(validUser) mustBe OK
    (contentAsJson(validUser) \ "isValid").as[Boolean] mustBe true

    val inputUser = User("sampleTest", "sample@email.com", "Sample", "User", "123456799")
    val request = FakeRequest(PUT, "/user").withHeaders("Host" -> "localhost").withBody(Json.obj("user" -> Json.toJson(inputUser), "timestamp" -> Json.toJson(new DateTime())))
    val addUser = route(app, request).get
    status(addUser) mustBe OK
    (contentAsJson(addUser) \ "success").as[Boolean] mustBe true

    val invalidUser = route(app, validUserPayload).get
    status(invalidUser) mustBe OK
    (contentAsJson(invalidUser) \ "isValid").as[Boolean] mustBe false

  }

}
