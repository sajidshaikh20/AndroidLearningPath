package com.base.hilt.ui.mvvm_clean.domain.uses_cases.get_data_profile

import com.base.hilt.network.FlowResponseHandler
import com.base.hilt.network.HttpErrorCode
import com.base.hilt.ui.mvvm_clean.data.getUserData.Carts
import com.base.hilt.ui.mvvm_clean.domain.repo.getUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class GetCartsUseCase @Inject constructor(private val repository: getUserRepository){


    operator fun invoke(): Flow<FlowResponseHandler<Carts?>> = flow {
        try {
            emit(FlowResponseHandler.Loading)
            when (val response = repository.myCarts()) {
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
