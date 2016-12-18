package dao

import model.User

trait UserDao {
  def getAllUsers(): List[User]


  /**
   * Database error handling required.
   * @param username
   * @return
   */
  def isValidUsername(username: String): Boolean

}

class UserDaoImpl {

}
