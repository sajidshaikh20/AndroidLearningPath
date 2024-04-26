package com.base.hilt.ui.getData.presentation


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.hilt.base.ViewModelBase
import com.base.hilt.network.ResponseHandler
import com.base.hilt.ui.getData.data.getUserData.GetUserData
import com.base.hilt.ui.getData.domain.uses_cases.GetDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetDataViewModel @Inject constructor(
    private val userProfileUseCase: GetDataUseCase) : ViewModelBase() {

    private val _userGetData = MutableLiveData<ResponseHandler<GetUserData?>>()

    val userGetData: LiveData<ResponseHandler<GetUserData?>> = _userGetData

    fun calluserGetDataAPI() {
        viewModelScope.launch {
            _userGetData.value = ResponseHandler.Loading
            val response = userProfileUseCase()
            _userGetData.value = response
        }
    }
}