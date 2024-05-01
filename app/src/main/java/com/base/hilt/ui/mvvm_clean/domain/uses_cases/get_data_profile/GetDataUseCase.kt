package com.base.hilt.ui.mvvm_clean.domain.uses_cases.get_data_profile

import com.base.hilt.network.ResponseHandler
import com.base.hilt.ui.mvvm_clean.data.getUserData.GetUserData
import com.base.hilt.ui.mvvm_clean.domain.repo.getUserRepository
import javax.inject.Inject


class GetDataUseCase @Inject constructor(private val repository: getUserRepository){
    suspend operator fun invoke(): ResponseHandler<GetUserData?> {
        return repository.myProfileData()
    }
}
