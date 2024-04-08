package com.base.hilt.ui.notifications.repository

import com.base.hilt.base.BaseRepository
import com.base.hilt.network.ApiInterface
import com.base.hilt.ui.notifications.model.MobileData
import com.base.hilt.ui.notifications.model.MobileDataItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


class NotificationRepository @Inject constructor(var apiInterface: ApiInterface): BaseRepository(){

    // calling api and setting to flow.
    // Assuming MobileData.MobileDataItem is the type emitted by your Flow
    suspend fun getMobileData(): Flow<MobileDataItem> {
        return flow {
            // Make the API call
            val response: Response<MobileData>? = apiInterface.getMobileData()
            response?.body()?.forEach {
                    emit(it)
                }

        }
    }




}