package com.immediatus.webApp
package domain.db

import com.mongodb.casbah.Imports._


class MongoRepository(
  mongodb               : MongoDB,
  usersCollectionKey    : String,
  messagesCollectionKey : String
)
  extends domain.RepositoryLocator
  with domain.UserRepository
  with domain.MessageRepository {

  import domain.{User, Message}

  private val ID      = "login"
  private val NAME    = "name"
  private val EMAIL   = "email"
  private val PASSWD  = "password"

  private val TIME    = "time"
  private val TEXT    = "text"

  private def err[T](field : String): T = throw NoFieldException(field)

  private def dboToUser(dbo : DBObject): User =
    User(
      login = dbo.getAsOrElse[String] (ID,     err(ID)),
      email = dbo.getAsOrElse[String] (EMAIL,  err(EMAIL)),
      name  = dbo.getAsOrElse[String] (NAME,   err(NAME))
    )

  private def dboToMessage(dbo : DBObject): Message =
    Message(
      userLogin = dbo.getAsOrElse[String] (ID,     err(ID)),
      time      = dbo.getAsOrElse[Long]   (TIME,   err(TIME)),
      text      = dbo.getAsOrElse[String] (TEXT,   err(TEXT))
    )


  // exception types
  case class NoFieldException(field : String) extends Throwable


  // RepositoryLocator implementation
  def users     : domain.UserRepository = this
  def messages  : domain.MessageRepository = this


  // UserRepository implementation
  def getUserByLogin(login : String, passwd : String): Option[User] =
    mongodb(usersCollectionKey)
      .findOne(MongoDBObject(ID -> login, PASSWD -> User.passwordHash(passwd, "")))
      .map { dboToUser }


  // MessageRepository implementation
  def getLastMessages(time : Long): Seq[Message] = Nil

  def getUserMessages(login : String, time : Long):Seq[Message] = Nil

  def addUserMessage(login : String, text : String): Boolean = false
}

