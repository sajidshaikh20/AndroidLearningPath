package com.base.hilt.ui.notifications.repository

import com.base.hilt.base.BaseRepository
import com.base.hilt.network.ApiInterface
import com.base.hilt.network.ResponseData
import com.base.hilt.network.ResponseHandler
import com.base.hilt.network.ResponseHandler1
import com.base.hilt.ui.notifications.model.MobileData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class NotificationRepository @Inject constructor(var apiInterface: ApiInterface):
    BaseRepository() {

    // calling api and setting to flow.
    // Assuming MobileData.MobileDataItem is the type emitted by your Flow
    suspend fun getMobileData(): Flow<MobileData.MobileDataItem> {
        return flow {
            // Make the API call
            val response: Response<MobileData>? = apiInterface.getMobileData()

            // Check if the response is successful
            if (response != null && response.isSuccessful) {
                // Iterate through the list of MobileDataItems and emit each item
                response.body()?.forEach {
                    emit(it)
                }
            } else {
                // Handle the case where the API call fails
                emit(MobileData.MobileDataItem())
            }
        }
    }

    suspend fun getMobileData1():Flow<ResponseHandler1<ResponseData<List<Any>>?>>  {
        return makeAPICall1 { apiInterface.getMobileData1() }
    }


}