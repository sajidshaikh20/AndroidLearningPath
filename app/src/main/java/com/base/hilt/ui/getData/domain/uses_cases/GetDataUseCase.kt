package com.base.hilt.ui.getData.domain.uses_cases

import com.base.hilt.base.BaseRepository
import com.base.hilt.network.ResponseHandler
import com.base.hilt.ui.getData.data.getUserData.GetUserData
import com.base.hilt.ui.getData.domain.repo.MyProfileRepository
import javax.inject.Inject


class GetDataUseCase @Inject constructor(private val repository2: MyProfileRepository) :BaseRepository(){
    suspend operator fun invoke(): ResponseHandler<GetUserData?> {
        return repository2.myProfileData()
    }
}
