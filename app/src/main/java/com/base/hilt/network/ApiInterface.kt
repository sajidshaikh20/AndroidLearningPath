package com.base.hilt.network

import com.base.hilt.ui.getData.data.getUserData.GetUserData
import com.base.hilt.ui.notifications.model.MobileData
import retrofit2.Response
import retrofit2.http.GET
import javax.inject.Singleton

/**
 * Interface used for api
 */
@Singleton
interface ApiInterface {

    @GET("https://api.restful-api.dev/objects")
    suspend fun getMobileData(): Response<MobileData>

    @GET("https://dummyjson.com/users/1")
    suspend fun getUserData() : Response<GetUserData>

}
