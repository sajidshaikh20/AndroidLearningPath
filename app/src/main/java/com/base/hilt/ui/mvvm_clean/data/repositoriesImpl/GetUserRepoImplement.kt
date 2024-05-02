package com.base.hilt.ui.mvvm_clean.data.repositoriesImpl

import com.base.hilt.base.BaseRepository
import com.base.hilt.network.FlowResponseHandler
import com.base.hilt.network.HttpErrorCode
import com.base.hilt.network.ResponseHandler
import com.base.hilt.ui.mvvm_clean.data.getUserData.Carts
import com.base.hilt.ui.mvvm_clean.data.getUserData.GetUserData
import com.base.hilt.ui.mvvm_clean.data.getUserData.ProductDetails
import com.base.hilt.ui.mvvm_clean.data.remote.GetUserProfileApi
import com.base.hilt.ui.mvvm_clean.domain.repo.getUserRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetUserRepoImplement @Inject constructor(
    private val apiInterface: GetUserProfileApi
) : getUserRepository, BaseRepository() {

    //get user data profile
    override suspend fun myProfileData(): ResponseHandler<GetUserData?> {
        val response = makeAPICallBase {
            apiInterface.getUserData()
        }
        return response
    }

    //get cart api call with with base
    override suspend fun myCarts(): FlowResponseHandler<Carts?> {
        val response = makeAPICallWithFlow {
            apiInterface.getCarts()
        }
        var result: FlowResponseHandler<Carts?> = FlowResponseHandler.Loading
        response.collect { result = it }
        return result
    }

    //product details api call
    override suspend fun productDetails(id: Int): FlowResponseHandler<ProductDetails?> {
        val responce = makeAPICallWithFlow {
            apiInterface.productDetails(id)
        }
        var result: FlowResponseHandler<ProductDetails?> = FlowResponseHandler.Loading
        responce.collect { result = it }

        return result
    }

    override suspend fun myProfileDataWithFlow(): FlowResponseHandler<GetUserData?> {
        return makeAPICallWithFlow {
            apiInterface.getUserDataFlow()
        }.map { responseHandler ->
            when (responseHandler) {
                is FlowResponseHandler.OnFailed -> responseHandler
                is FlowResponseHandler.Loading -> responseHandler
                is FlowResponseHandler.OnSuccessResponse -> {
                    responseHandler.response?.let { FlowResponseHandler.OnSuccessResponse(it) }
                        ?: FlowResponseHandler.OnFailed(
                            HttpErrorCode.NOT_DEFINED.code,
                            "Response body is null",
                            ""
                        )
                }
            }
        }.single()
    }

}