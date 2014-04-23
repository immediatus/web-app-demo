package com.immediatus.webApp
package web.service

import com.typesafe.config.Config

final case class Settings(config: Config) {

  import config._

  lazy val host = getString("server.host")
  lazy val port = getInt("server.port")
  lazy val timeout = millis("server.timeout")

  private def millis(name: String): Int = getMilliseconds(name).toInt
  private def seconds(name: String): Int = millis(name) / 1000
}
