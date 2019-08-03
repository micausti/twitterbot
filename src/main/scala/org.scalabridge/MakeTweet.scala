package org.scalabridge

import java.util.UUID.randomUUID

import com.typesafe.scalalogging.Logger


  class MakeTweet {

    val logger = Logger("MakeTweet logger")

    def filePath(): String = {
      "/tmp/" + randomUUID().toString + ".png"
    }

    def status(today: String): String = {
      "Happy " + today + "!"
    }
  }


