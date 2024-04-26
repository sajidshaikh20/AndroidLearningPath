package com.base.hilt.ui.getData.data.remote

import com.base.hilt.ui.getData.data.getUserData.GetUserData
import retrofit2.Response
import retrofit2.http.GET
import javax.inject.Singleton


@Singleton
interface ProfileApi {

    @GET("https://dummyjson.com/users/1")
    suspend fun getUserData() : Response<GetUserData>
}