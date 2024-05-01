package com.base.hilt.network




sealed class FlowResponseHandler<out T> {
    data object Loading : FlowResponseHandler<Nothing>()
    class OnFailed<T>(val code: Int?,val messageCode: String?,val message: String?,val data: T? = null) :
        FlowResponseHandler<T>()
    class OnSuccessResponse<T>(val response: T) : FlowResponseHandler<T>()
}
