package com.base.hilt.ui.mvvm_clean.domain.uses_cases.login_usecase

import com.base.hilt.network.FlowResponseHandler
import com.base.hilt.network.HttpErrorCode
import com.base.hilt.ui.mvvm_clean.data.getUserData.LoginRequestBody
import com.base.hilt.ui.mvvm_clean.data.getUserData.LoginResponceWithClean
import com.base.hilt.ui.mvvm_clean.domain.repo.SocialLoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCases @Inject constructor(private val repository: SocialLoginRepository) {


    operator fun invoke(body: LoginRequestBody): Flow<FlowResponseHandler<LoginResponceWithClean?>> =
        flow {
            try {
                emit(FlowResponseHandler.Loading)
                when (val response = repository.loginWithClean(body)) {
                    is FlowResponseHandler.Loading -> emit(FlowResponseHandler.Loading)
                    is FlowResponseHandler.OnFailed -> emit(
                        FlowResponseHandler.OnFailed(
                            response.code, response.messageCode, response.message
                        )
                    )

                    is FlowResponseHandler.OnSuccessResponse -> emit(
                        FlowResponseHandler.OnSuccessResponse(response.response)
                    )
                }
            } catch (e: Exception) {
                emit(
                    FlowResponseHandler.OnFailed(
                        HttpErrorCode.NOT_DEFINED.code, HttpErrorCode.NOT_DEFINED.message, e.message
                    )
                )
            }
        }

}