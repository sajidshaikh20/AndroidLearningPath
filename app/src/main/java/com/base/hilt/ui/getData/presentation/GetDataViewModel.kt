package com.base.hilt.ui.getData.presentation


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.hilt.base.ViewModelBase
import com.base.hilt.network.ResponseHandler
import com.base.hilt.ui.getData.data.GetUserData
import com.base.hilt.ui.getData.domain.uses_cases.GetDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetDataViewModel @Inject constructor(private val userProfileUseCase: GetDataUseCase,val repository: GetDataRepo) : ViewModelBase() {

    val userGetData =
        MutableLiveData<ResponseHandler<GetUserData?>>()

    fun calluserGetDataAPI() {
        viewModelScope.launch {
            userGetData.postValue(ResponseHandler.Loading)
            userGetData.value = repository.getdataUser()
        }
    }
}