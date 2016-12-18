package dao

trait UserDao {

  /**
   * Database error handling required.
   * @param username
   * @return
   */
  def isValidUsername(username:String): Boolean

}

class UserDaoImpl {

}
