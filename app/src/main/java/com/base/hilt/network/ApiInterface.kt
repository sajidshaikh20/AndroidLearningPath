package com.base.hilt.network

import com.base.hilt.ConfigFiles
import com.base.hilt.ui.getData.data.GetUserData
import com.base.hilt.ui.getData.domain.repository.GetDataRepository
import com.base.hilt.ui.notifications.model.MobileData
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
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
