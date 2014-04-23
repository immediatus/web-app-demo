package com.immediatus.webApp
package domain.db

import com.typesafe.config.Config

final case class Settings(config: Config) {
  import config._

  lazy val host                   = getString("database.host")
  lazy val name                   = getString("database.name")
  lazy val usersCollectionKey     = getString("database.collections.usersKey")
  lazy val messagesCollectionKey  = getString("database.collections.messagesKey")
}
