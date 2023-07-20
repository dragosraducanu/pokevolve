package com.dragos.domain.util

import java.net.URI

object UrlUtils {
    fun getIdFromUrl(uri: String): Int? {
      return try {
          val paths = URI(uri).path.split("/")
          Integer.parseInt(paths.last { it.isNotBlank() })
      } catch (e: Exception) {
          e.printStackTrace()
          null
      }
    }
}