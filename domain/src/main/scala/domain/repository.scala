package com.immediatus.webApp
package domain

trait RepositoryLocator {
  def users : UserRepository
  def messages : MessageRepository
}

trait UserRepository {
  def getUserByLogin(login : String, passwd : String): Option[User]
}

trait MessageRepository {
  def getLastMessages(time : Long): Seq[Message]
  def getUserMessages(login : String, time : Long): Seq[Message]
  def addUserMessage(login : String, text : String): Boolean
}
