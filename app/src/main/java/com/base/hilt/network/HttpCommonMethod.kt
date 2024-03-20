package com.base.hilt.network

import com.apollographql.apollo3.api.Error
import com.base.hilt.utils.Constants
import com.base.hilt.utils.Validation
import com.fasterxml.jackson.databind.JsonNode

object HttpCommonMethod {

    /**
     * check whether API response is success or not
     */
    fun isSuccessResponse(responseCode: Int): Boolean {
        return responseCode in 200..300
    }

    /**
     * check Error Message
     */
    fun getErrorMessage(error: JsonNode?): String {
        var value = ""
        if (error != null) {
            when {
                error.isArray -> {
                    for (objInArray in error) {
                        value = (objInArray).toString()
                    }
                }
                error.isContainerNode -> {
                    val it: Iterator<Map.Entry<String, JsonNode>> = error.fields()
                    while (it.hasNext()) {
                        val field = it.next()
                        value = if (Validation.isNotNull(value)) {
                            value + "," + removeArrayBrace(field.value.toString())
                        } else {
                            removeArrayBrace(field.value.toString())
                        }
                    }
                }
                else -> {
                    value = (error).toString()
                }
            }
        }
        return value
    }

    /**
     * Remove [] from Error Objects when there are multiple errors
     *
     * @param message as String
     * @return replacedString
     */
    fun removeArrayBrace(message: String): String {
        return message.replace("[\"", "").replace("\"]", "").replace(".", "")
    }

    private const val AUTHORIZATION = "authentication"
    private const val RESPONSE_CATEGORY = "category"
    private const val META = "meta"
    private const val VALIDATION = "validation"
    private const val CUSTOM = "Custom"
    @Suppress("UNCHECKED_CAST")
    fun getErrorMessageForGraph(error: List<Error>?): Triple<Int, String, String> {
        var value = ""
        var statusCode = -1
        val meta = (error?.get(0)?.extensions?.get(META) as LinkedHashMap<*, *>?)
        val messageCode = (meta?.get(Constants.MESSAGE_CODE) as? String).orEmpty()
        when (error?.get(0)?.extensions?.get(RESPONSE_CATEGORY)) {
            AUTHORIZATION -> {
                value = Constants.TOKEN_EXPIRED
                statusCode = HttpErrorCode.UNAUTHORIZED.code
            }
            VALIDATION -> {
                statusCode = HttpErrorCode.SERVER_SIDE_VALIDATION.code
                val validation = (error[0].extensions?.get(VALIDATION) as LinkedHashMap<*, *>)
                val keys = validation.keys
                for (i in keys) {
                    value = if (Validation.isNotNull(value)) {
                        value + Constants.COMMA + (validation[i] as ArrayList<String>)[0]
                    } else {
                        (validation[i] as ArrayList<String>)[0]
                    }
                }
            }
            CUSTOM -> {
                statusCode = HttpErrorCode.BAD_RESPONSE.code
                value = error[0].extensions?.get(Constants.MESSAGE) as? String ?: error[0].message
            }
        }
        return Triple(statusCode, value, messageCode)
    }
}
