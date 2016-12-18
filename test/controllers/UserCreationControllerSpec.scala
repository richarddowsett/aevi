package controllers

import org.scalatestplus.play._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._

class UserCreationControllerSpec extends PlaySpec with OneAppPerTest {

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
      val request = FakeRequest(GET, "/username").withHeaders("Host" -> "localhost").withBody(Json.obj("username" -> "richarddowsett"))
      val validUser = route(app, request).get

      status(validUser) mustBe OK
      contentType(validUser) mustBe Some("application/json")
      // expecting {success: true}
      (contentAsJson(validUser) \ "validUser").getOrElse(fail("Failed to find validUser field")) mustEqual true
    }
  }
  // todo add username to database and expect a failure
}
