package com.base.hilt.ui.mvvm_clean.data.repositoriesImpl

import com.base.hilt.base.BaseRepository
import com.base.hilt.network.FlowResponseHandler
import com.base.hilt.ui.mvvm_clean.data.getUserData.LoginRequestBody
import com.base.hilt.ui.mvvm_clean.data.getUserData.LoginResponceWithClean
import com.base.hilt.ui.mvvm_clean.data.remote.LoginInterface
import com.base.hilt.ui.mvvm_clean.domain.repo.SocialLoginRepository
import javax.inject.Inject
import javax.inject.Singleton




@Singleton
class SocialLoginRepoImplement @Inject constructor(
    private val apiInterface: LoginInterface
) : SocialLoginRepository, BaseRepository() {

    override suspend fun loginWithClean(body: LoginRequestBody): FlowResponseHandler<LoginResponceWithClean?> {
        val response = makeAPICallWithFlow {
           apiInterface.loginWithClean(body)
        }
        var result: FlowResponseHandler<LoginResponceWithClean?> = FlowResponseHandler.Loading
        response.collect { result = it }
        return result
    }
}