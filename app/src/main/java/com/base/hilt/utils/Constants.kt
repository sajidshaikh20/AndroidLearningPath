package com.base.hilt.utils

import com.base.hilt.BuildConfig
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

object Constants {
    val JSON = jacksonObjectMapper()
    val PREF_NAME = BuildConfig.APPLICATION_ID
    const val UUID = "uuid"
    const val MESSAGE_CODE = "message_code"
    const val MESSAGE = "message"
    const val TOKEN_EXPIRED = "Your session has been expired. Please login again"
    const val COMMA = ","
    const val EMPTY = ""
}