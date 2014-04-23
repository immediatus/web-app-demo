package com.immediatus.webApp
package domain


case class User (
  login     : String,
  email     : String,
  name      : String
)

object User {
  def passwordHash(passwd : String, salt : String): String = passwd
}
