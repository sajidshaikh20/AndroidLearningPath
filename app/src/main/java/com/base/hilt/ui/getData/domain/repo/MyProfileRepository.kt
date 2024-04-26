package com.base.hilt.ui.getData.domain.repo

import com.base.hilt.network.ResponseHandler
import com.base.hilt.ui.getData.data.getUserData.GetUserData

interface MyProfileRepository {
    suspend fun myProfileData(): ResponseHandler<GetUserData?>
}
