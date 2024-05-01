package com.base.hilt.ui.mvvm_clean.data.remote

import com.base.hilt.ui.mvvm_clean.data.getUserData.Carts
import com.base.hilt.ui.mvvm_clean.data.getUserData.GetUserData
import com.base.hilt.ui.mvvm_clean.data.getUserData.ProductDetails
import com.base.hilt.utils.EndPoints
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Singleton


@Singleton
interface GetUserProfileApi {

    @GET(EndPoints.USER_DATA)
    suspend fun getUserData() : Response<GetUserData>

    @GET(EndPoints.CARTS)
    suspend fun getCarts(): Response<Carts>

    @GET(EndPoints.PRODUCT)
    suspend fun productDetails(@Path("productId") productId: Int): Response<ProductDetails>

    @GET(EndPoints.USER_DATA)
    suspend fun getUserDataFlow() : Response<GetUserData>
}