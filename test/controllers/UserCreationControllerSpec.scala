package controllers

import model.User
import modules.UserDaoMockModule
import org.joda.time.DateTime
import org.mockito.ArgumentMatchers
import org.mockito.Mockito._
import org.scalatest.TestData
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play._
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{JsValue, Json}
import play.api.test.Helpers._
import play.api.test._
import org.mockito.ArgumentMatchers._

import scala.concurrent.Future
import scalaz.\/-


class UserCreationControllerSpec extends PlaySpec with OneAppPerTest with MockitoSugar {
  val userDaoMockModule: UserDaoMockModule = new UserDaoMockModule

  override def newAppForTest(testData: TestData): Application = {

    new GuiceApplicationBuilder().overrides(userDaoMockModule).build()
  }

  val mock = userDaoMockModule.userDaoMock

  "UserCreationController PUT /user" should {
    "add a user with no error" in {
      val inputUser = User("sample","sample@email.com", "Sample", "User", "123456799")
      when(mock.addUser(ArgumentMatchers.eq(inputUser))).thenReturn(Future.successful(\/-(())))
      val request = FakeRequest(PUT, "/user").withHeaders("Host" -> "localhost").withBody(Json.obj("user" -> Json.toJson(inputUser), "timestamp" -> Json.toJson(new DateTime())))
      val addUser = route(app, request).get

      status(addUser) mustEqual OK
      contentType(addUser) mustEqual Some("application/json")
      (contentAsJson(addUser) \ "success").as[Boolean] mustEqual true
      (contentAsJson(addUser) \ "username").as[String] mustEqual "sample"
    }
  }

  "UserCreationController GET /" should {

    "render the index page from the router" in {
      when(mock.getAllUsers()).thenReturn(Future.successful(List(User("richarddowsett@email.com", "richarddowsett@email.com", "Richard", "Dowsett", "01234567890"))))
      val request = FakeRequest(GET, "/").withHeaders("Host" -> "localhost")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("application/json")
      val result = contentAsJson(home).validate[List[User]].fold(e => fail("Failed to parse list of users"), identity)
      result must have size 1

    }
  }


  "UserCreationController GET /username" should {

    "validate a username" in {
      when(mock.isValidUsername("richarddowsett")).thenReturn(Future.successful(true))
      val request = FakeRequest(GET, "/username").withHeaders("Host" -> "localhost").withBody(Json.obj("username" -> "richarddowsett"))
      val validUser = route(app, request).get

      status(validUser) mustBe OK
      contentType(validUser) mustBe Some("application/json")
      // expecting {username: "richarddowsett", isValid: true}
      (contentAsJson(validUser) \ "isValid").as[Boolean] mustEqual true
      (contentAsJson(validUser) \ "username").as[String] mustEqual "richarddowsett"
      verify(mock, times(1)).isValidUsername("richarddowsett")
    }

    "validate that a username is not valid" in {
      when(mock.isValidUsername("invalid")).thenReturn(Future.successful(false))
      val request = FakeRequest(GET, "/username").withHeaders("Host" -> "localhost").withBody(Json.obj("username" -> "invalid"))
      val validUser = route(app, request).get

      status(validUser) mustBe OK
      contentType(validUser) mustBe Some("application/json")
      // expecting {username: "invalid", isValid: false}
      (contentAsJson(validUser) \ "isValid").as[Boolean] mustEqual false
      (contentAsJson(validUser) \ "username").as[String] mustEqual "invalid"
      verify(mock, times(1)).isValidUsername("invalid")
    }
  }
}
