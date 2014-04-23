package com.immediatus.webApp
package domain

import com.mongodb.casbah.Imports._

trait DB {
  def repo: domain.RepositoryLocator
}

object DB {
  def mongoDB(settings : db.Settings) = {
    val mongodb = MongoClient(settings.host)(settings.name)

    new DB {
      def repo =
        new db.MongoRepository(
          mongodb,
          settings.usersCollectionKey,
          settings.messagesCollectionKey)
    }
  }
}

