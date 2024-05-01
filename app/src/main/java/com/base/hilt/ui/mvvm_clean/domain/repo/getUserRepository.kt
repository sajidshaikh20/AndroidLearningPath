package com.base.hilt.ui.mvvm_clean.domain.repo

import com.base.hilt.network.FlowResponseHandler
import com.base.hilt.network.ResponseHandler
import com.base.hilt.ui.mvvm_clean.data.getUserData.Carts
import com.base.hilt.ui.mvvm_clean.data.getUserData.GetUserData
import com.base.hilt.ui.mvvm_clean.data.getUserData.ProductDetails

interface getUserRepository {

    suspend fun myProfileData(): ResponseHandler<GetUserData?>

    suspend fun myCarts(): FlowResponseHandler<Carts?>

    suspend fun productDetails(id: Int) : FlowResponseHandler<ProductDetails?>

    suspend fun myProfileDataWithFlow(): FlowResponseHandler<GetUserData?>
}
