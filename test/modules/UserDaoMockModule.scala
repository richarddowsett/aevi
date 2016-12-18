package modules

import com.google.inject.AbstractModule
import dao.UserDao
import org.mockito._
import org.scalatest.mock.MockitoSugar

class UserDaoMockModule extends AbstractModule with MockitoSugar {
  override def configure(): Unit = {
    bind(classOf[UserDao]).toInstance(userDaoMock)
  }

  val userDaoMock = mock[UserDao]
}
