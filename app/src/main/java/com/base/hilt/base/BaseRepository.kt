package com.base.hilt.base

import android.accounts.NetworkErrorException
import android.util.Log
import com.apollographql.apollo3.api.ApolloResponse
import com.base.hilt.network.*
import com.base.hilt.utils.Constants.JSON
import com.base.hilt.utils.DebugLog
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import okhttp3.internal.http2.ConnectionShutdownException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.exception.ApolloHttpException
import com.apollographql.apollo3.exception.ApolloNetworkException
import com.base.hilt.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


/**
 * Common class for API calling
 */

open class BaseRepository {

    /**
     * This is the Base suspended method which is used for making the call of an Api and
     * Manage the Response with response code to display specific response message or code.
     * @param call ApiInterface method defination to make a call and get response from generic Area.
     */
    suspend fun <T : Any> makeAPICallBase(call: suspend () -> Response<T>): ResponseHandler<T?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = call.invoke()
                when {
                    response.code() in (200..300) -> {
                        return@withContext when (response.code()) {
                            400 -> ResponseHandler.OnFailed(
                                response.code(),
                                response.message(),
                                "0"
                            )

                            401 -> ResponseHandler.OnFailed(
                                HttpErrorCode.UNAUTHORIZED.code,
                                response.message(),
                                response.code().toString()
                            )

                            else -> ResponseHandler.OnSuccessResponse(response.body())
                        }
                    }

                    response.code() == 401 -> {
                        return@withContext parseUnAuthorizeResponse(response.errorBody())
                    }

                    response.code() == 422 -> {
                        return@withContext parseServerSideErrorResponse(response.errorBody())
                    }

                    response.code() == 500 -> {
                        return@withContext ResponseHandler.OnFailed(
                            HttpErrorCode.NOT_DEFINED.code,
                            "",
                            response.message()
                        )
                    }

                    else -> {
                        return@withContext parseUnKnownStatusCodeResponse(response.errorBody())
                    }
                }
            } catch (e: Exception) {
                DebugLog.print(e)
                return@withContext when {
                    e is UnknownHostException || e is ConnectionShutdownException ->
                        ResponseHandler.OnFailed(HttpErrorCode.NO_CONNECTION.code, "", "")

                    e is SocketTimeoutException || e is IOException || e is NetworkErrorException ->
                        ResponseHandler.OnFailed(HttpErrorCode.NOT_DEFINED.code, "", "")

                    else -> ResponseHandler.OnFailed(HttpErrorCode.NOT_DEFINED.code, "", "")
                }
            }
        }
    }


    suspend fun <T : Any> makeAPICall(call: suspend () -> Response<ResponseData<T>>): ResponseHandler<ResponseData<T>?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = call.invoke()
                when {
                    response.code() in (200..300) -> {
                        return@withContext when (response.body()?.meta?.statusCode) {
                            400 -> {

                                ResponseHandler.OnFailed(
                                    response.body()?.meta?.statusCode!!,
                                    response.body()?.meta?.message!!,
                                    "0"
                                )
                            }
                            401 -> {
                                ResponseHandler.OnFailed(
                                    HttpErrorCode.UNAUTHORIZED.code,
                                    response.body()?.meta?.message!!,
                                    response.body()?.meta?.statusCode!!.toString()
                                )
                            }
                            else -> ResponseHandler.OnSuccessResponse(response.body())
                        }
                    }
                    response.code() == 401 -> {
                        return@withContext parseUnAuthorizeResponse(response.errorBody())
                    }
                    response.code() == 422 -> {
                        return@withContext parseServerSideErrorResponse(response.errorBody())
                    }
                    response.code() == 500 -> {
                        return@withContext ResponseHandler.OnFailed(
                            HttpErrorCode.NOT_DEFINED.code,
                            "",
                            response.body()?.meta?.messageCode.toString()
                        )
                    }
                    else -> {
                        return@withContext parseUnKnownStatusCodeResponse(response.errorBody())
                    }
                }
            } catch (e: Exception) {
                DebugLog.print(e)
                return@withContext if (
                    e is UnknownHostException ||
                    e is ConnectionShutdownException
                ) {
                    ResponseHandler.OnFailed(HttpErrorCode.NO_CONNECTION.code, "", "")
                } else if (e is SocketTimeoutException || e is IOException ||
                    e is NetworkErrorException
                ) {
                    ResponseHandler.OnFailed(HttpErrorCode.NOT_DEFINED.code, "", "")
                } else {
                    ResponseHandler.OnFailed(HttpErrorCode.NOT_DEFINED.code, "", "")
                }
            }
        }
    }

    /**
     * Response parsing for 401 status code
     **/
    private fun parseUnAuthorizeResponse(response: ResponseBody?): ResponseHandler.OnFailed<Nothing> {
        val message: String
        val bodyString = response?.string() ?: Constants.EMPTY
        val responseWrapper: ErrorWrapper = JSON.readValue(bodyString)
        message = if (responseWrapper.meta?.statusCode == 401) {
            if (responseWrapper.errors != null) {
                HttpCommonMethod.getErrorMessage(responseWrapper.errors)
            } else {
                responseWrapper.meta.message.toString()
            }
        } else {
            responseWrapper.meta?.message.toString()
        }
        return ResponseHandler.OnFailed(
            data = null,
            code = HttpErrorCode.UNAUTHORIZED.code,
            message = message,
            messageCode = responseWrapper.meta?.messageCode.toString()
        )
    }

    /**
     * Response parsing for 422 status code
     * */
    private fun parseServerSideErrorResponse(response: ResponseBody?): ResponseHandler.OnFailed<Nothing> {
        val message: String
        val bodyString = response?.string()
        val responseWrapper: ErrorWrapper = JSON.readValue(bodyString!!)

        message = if (responseWrapper.meta!!.statusCode == 422) {
            if (responseWrapper.errors != null) {
                HttpCommonMethod.getErrorMessage(responseWrapper.errors)
            } else {
                responseWrapper.meta.message.toString()
            }
        } else {
            responseWrapper.meta.message.toString()
        }
        return ResponseHandler.OnFailed(
            HttpErrorCode.EMPTY_RESPONSE.code,
            message,
            responseWrapper.meta.messageCode.toString()
        )
    }

    /**
     * Response parsing for unknown status code
     * */
    private fun parseUnKnownStatusCodeResponse(response: ResponseBody?): ResponseHandler.OnFailed<Nothing> {
        val bodyString = response?.string()
        val responseWrapper: ErrorWrapper = JSON.readValue(bodyString!!)
        val message = if (responseWrapper.meta!!.statusCode == 422) {
            if (responseWrapper.errors != null) {
                HttpCommonMethod.getErrorMessage(responseWrapper.errors)
            } else {
                responseWrapper.meta.message.toString()
            }
        } else {
            responseWrapper.meta.message.toString()
        }
        return if (message.isEmpty()) {
            ResponseHandler.OnFailed(
                HttpErrorCode.EMPTY_RESPONSE.code,
                message,
                responseWrapper.meta.messageCode.toString()
            )
        } else {
            ResponseHandler.OnFailed(
                HttpErrorCode.NOT_DEFINED.code,
                message,
                responseWrapper.meta.messageCode.toString()
            )
        }
    }

    /**
     * This is the Base suspended method which is used for making the call of an Api using graphql
     * Created Sajid shaikh
    20/march/2024
    graphql base
     */

    suspend fun <T : Operation.Data> graphQlApiCall(call: suspend () -> ApolloResponse<T>): ResponseHandler<ApolloResponse<T>> {
        try {
            val response = call.invoke()
            when {
                response == null -> {
                    return ResponseHandler.OnFailed(
                        code = HttpErrorCode.BAD_RESPONSE.code,
                        message = HttpErrorCode.BAD_RESPONSE.message,
                        messageCode = null,
                    )
                }

                response.hasErrors() -> {

                    val errorModel = HttpCommonMethod.getErrorMessageForGraph(
                        response.errors
                    )

                    //                val error = response.errors?.let { GraphQLErrors(it) }
                    //                Log.i("madmad", "onLoginApi: here2")
                    return ResponseHandler.OnFailed(
                        code = errorModel.first,
                        message = errorModel.second,
                        messageCode = errorModel.third,
                    )
                }

                else -> {
                    return ResponseHandler.OnSuccessResponse(response)
                }
            }

        } catch (e: java.lang.Exception) {
            when (e) {
                is ApolloNetworkException -> {
                    return ResponseHandler.OnFailed(
                        code = HttpErrorCode.NO_CONNECTION.code,
                        message = HttpErrorCode.NO_CONNECTION.message,
                        messageCode = null,
                    )
                }

                is ApolloHttpException -> {
                    return ResponseHandler.OnFailed(
                        code = HttpErrorCode.BAD_RESPONSE.code,
                        message = e.message,
                        messageCode = null,
                    )
                }

                else -> {
                    return ResponseHandler.OnFailed(
                        code = HttpErrorCode.BAD_RESPONSE.code,
                        message = e.message,
                        messageCode = null,
                    )
                }

            }
        }
    }


    suspend fun <T : Any> makeAPICall1(
        call: suspend () -> Response<ResponseData<T>>
    ): Flow<ResponseHandler1<ResponseData<T>?>> {

        return flow {

            emit(ResponseHandler1.Loading)

            var response:Response<ResponseData<T>>? = null
            var str:String?  = ""

            try {
                response = call.invoke()
                str = response.errorBody()?.string()
            }catch (e:Exception){
                e.printStackTrace()
                e.message?.let { Log.d("API_EXCEPTION", it) }
            }

            try {
                when (response?.code()) {

                    401 -> {
                        emit(ResponseHandler1.Loading)
                        emit(parseUnAuthorizeResponse1(str))
                    }

                    404 ->{

                        if(!str.isNullOrEmpty()){
                            emit(ResponseHandler1.Loading)
                            emit(parseUnAuthorizeResponse1(str))
                        }
                    }

                    422 -> {
                        if (response.message().equals(HttpErrorCode.UNAUTHORIZED)) {
                            emit(ResponseHandler1.Loading)
                        } else {
                            emit(ResponseHandler1.Loading)
                            emit(parseUnAuthorizeResponse1(str!!))
                        }

                    }

                    500 -> {
                        emit(ResponseHandler1.Loading)
                        emit(
                            ResponseHandler1.OnFailed(
                                HttpErrorCode.NOT_DEFINED.code,
                                response.body()?.meta?.message!!,
                                response.body()?.meta?.messageCode.toString()
                            )
                        )
                    }

                    in (200..300) -> {

                        when (response?.body()?.meta?.statusCode) {
                            400 -> {
                                emit(ResponseHandler1.Loading)
                                ResponseHandler1.OnFailed(
                                    response.body()?.meta?.statusCode!!,
                                    response.body()?.meta?.message!!,
                                    "0"
                                )
                            }
                            401 -> {
                                if (response.body()?.meta?.messageCode!!.equals(HttpErrorCode.UNAUTHORIZED)) {
                                    emit(ResponseHandler1.Loading)
//                                    navController.navigate(R.id.loginFragment)
                                } else {
                                    emit(ResponseHandler1.Loading)
                                    emit(
                                        ResponseHandler1.OnFailed(
                                            HttpErrorCode.UNAUTHORIZED.code,
                                            response.body()?.meta?.message!!,
                                            response.body()?.meta?.statusCode!!.toString()
                                        )
                                    )
                                }
                            }
                            else -> {
                                emit(ResponseHandler1.Loading)
                                emit(ResponseHandler1.OnSuccessResponse(response?.body()))
                            }
                        }
                    }

                    else -> {
                        emit(ResponseHandler1.Loading)
                        emit(parseUnAuthorizeResponse1(str))
                    }
                }
            } catch (e: Exception) {
                DebugLog.print(e)
                emit(ResponseHandler1.Loading)
                emit(ResponseHandler1.OnFailed(HttpErrorCode.NOT_DEFINED.code, e.message!!, ""))
            }

        }.flowOn(Dispatchers.IO).catch {}
    }

    /**
     * Response parsing for 401 status code
     **/
    private fun parseUnAuthorizeResponse1(bodyString: String?): ResponseHandler1.OnFailed {
        val message: String
        val responseWrapper: ErrorWrapper = JSON.readValue(bodyString!!)
        message = if (responseWrapper.meta!!.statusCode == 401) {
            if (responseWrapper.errors != null) {
                HttpCommonMethod.getErrorMessage(responseWrapper.errors)
            } else {
                responseWrapper.meta.message.toString()
            }
        } else {
            responseWrapper.meta.message.toString()
        }
        return ResponseHandler1.OnFailed(
            HttpErrorCode.UNAUTHORIZED.code,
            message,
            responseWrapper.meta.messageCode.toString()
        )
    }

}
