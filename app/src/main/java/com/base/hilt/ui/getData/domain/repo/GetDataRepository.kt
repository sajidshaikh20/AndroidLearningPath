package com.base.hilt.ui.getData.domain.repo

import com.base.hilt.base.BaseRepository
import com.base.hilt.network.ApiInterface
import com.base.hilt.network.ResponseHandler
import com.base.hilt.ui.getData.data.getUserData.GetUserData
import javax.inject.Inject


//class GetDataRepository @Inject constructor(var apiInterface: ApiInterface) :
//    BaseRepository() {
//    suspend fun getdataUser(): ResponseHandler<GetUserData?> {
//        return makeAPICallBase {
//            apiInterface.getUserData()
//        }
//    }
//}