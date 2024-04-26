package com.base.hilt.ui.getData.data.repositoriesImpl

import com.base.hilt.base.BaseRepository
import com.base.hilt.network.ApiInterface
import com.base.hilt.network.ResponseHandler
import com.base.hilt.ui.getData.data.getUserData.GetUserData
import com.base.hilt.ui.getData.data.remote.ProfileApi
import com.base.hilt.ui.getData.domain.repo.MyProfileRepository
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MyProfileRepImplement @Inject constructor(
    private val apiInterface: ApiInterface
) : MyProfileRepository, BaseRepository() {
    override suspend fun myProfileData(): ResponseHandler<GetUserData?> {
        val response = makeAPICallBase {
            apiInterface.getUserData()
        }
        return  response
    }
}