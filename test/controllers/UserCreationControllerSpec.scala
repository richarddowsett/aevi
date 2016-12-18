package controllers

import modules.UserDaoMockModule
import org.mockito.Mockito._
import org.scalatest.TestData
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play._
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._

class UserCreationControllerSpec extends PlaySpec with OneAppPerTest with MockitoSugar {
  val userDaoMockModule: UserDaoMockModule = new UserDaoMockModule

  override def newAppForTest(testData: TestData): Application = {

    new GuiceApplicationBuilder().overrides(userDaoMockModule).build()
  }

  val mock = userDaoMockModule.userDaoMock
  "UserCreationController GET /" should {

    "render the index page from the router" in {
      val request = FakeRequest(GET, "/").withHeaders("Host" -> "localhost")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("application/json")
    }

    "render the index page from the application" in {
      val controller = app.injector.instanceOf[UserCreationController]
      val home = controller.allUsers().apply(FakeRequest())

      status(home) mustBe OK
      contentType(home) mustBe Some("application/json")
    }
  }


  "UserCreationController GET /username" should {

    "validate a username" in {
      when(mock.isValidUsername("richarddowsett")).thenReturn(true)
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
      when(mock.isValidUsername("invalid")).thenReturn(false)
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
