package com.base.hilt.ui.mvvm_clean.data.remote


import com.base.hilt.ui.mvvm_clean.data.getUserData.LoginRequestBody
import com.base.hilt.ui.mvvm_clean.data.getUserData.LoginResponceWithClean
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Singleton


@Singleton
interface LoginInterface {
    @POST("https://recruitment-api.pyt1.stg.jmr.pl/login")
    suspend fun loginWithClean(@Body model: LoginRequestBody) : Response<LoginResponceWithClean>
}