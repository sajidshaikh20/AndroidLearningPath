package com.base.hilt.ui.mvvm_clean.domain.repo

import com.base.hilt.network.FlowResponseHandler
import com.base.hilt.ui.mvvm_clean.data.getUserData.LoginRequestBody
import com.base.hilt.ui.mvvm_clean.data.getUserData.LoginResponceWithClean


interface SocialLoginRepository {
    suspend fun loginWithClean(body: LoginRequestBody) : FlowResponseHandler<LoginResponceWithClean?>
}