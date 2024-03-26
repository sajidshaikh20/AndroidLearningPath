package com.base.hilt.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response


class AuthorizationIntercepter() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val token = HttpCommonMethod.getAuthToken()
        Log.i("restapi", "intercept: ${HttpCommonMethod.getAuthToken()} ")
        if (token.isNotEmpty()) {
            request.addHeader("Authorization", HttpCommonMethod.getAuthToken())
        }
        return chain.proceed(request.build())
    }
}