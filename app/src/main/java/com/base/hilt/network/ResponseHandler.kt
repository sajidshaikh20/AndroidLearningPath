package com.base.hilt.network

/**
 * Package Name : com.appname.structure.network.Client
 * Project Name : BrainvireStructure
 */

sealed class ResponseHandler<out T> {
    object Loading : ResponseHandler<Nothing>()
    class OnFailed<T>(val code: Int?,val messageCode: String?,val message: String?,val data: T? = null) :
        ResponseHandler<T>()
    class OnSuccessResponse<T>(val response: T) : ResponseHandler<T>()
}

sealed class ResponseHandler1<out T> {
    object Loading : ResponseHandler1<Nothing>()
    class OnFailed(val code: Int, val message: String, val messageCode: String) :
        ResponseHandler1<Nothing>()
    class OnSuccessResponse<T>(val response: T) : ResponseHandler1<T>()
}