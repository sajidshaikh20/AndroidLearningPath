package com.base.hilt.ui.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.ApolloResponse
import com.base.hilt.LoginMutation
import com.base.hilt.base.ViewModelBase
import com.base.hilt.di.UserRepository
import com.base.hilt.network.ResponseHandler
import com.base.hilt.type.LoginInput
import com.base.hilt.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(val repository: UserRepository) :
    ViewModelBase() {

    val loginLiveData = SingleLiveEvent<ResponseHandler<ApolloResponse<LoginMutation.Data>>?>()
    fun loginApi(loginReq: LoginInput) {
        Log.i("2181", "loginApi: $loginReq")
        viewModelScope.launch {
            loginLiveData.postValue(ResponseHandler.Loading)
            val result = repository.onLoginApi(loginReq)
            loginLiveData.postValue(result)
        }
    }


}