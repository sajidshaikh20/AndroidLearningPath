package com.base.hilt.ui.mvvm_clean.presentation.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.ApolloResponse
import com.base.hilt.LoginMutation
import com.base.hilt.base.ViewModelBase
import com.base.hilt.di.UserRepository
import com.base.hilt.network.FlowResponseHandler
import com.base.hilt.network.ResponseHandler
import com.base.hilt.type.LoginInput
import com.base.hilt.ui.mvvm_clean.data.getUserData.LoginRequestBody
import com.base.hilt.ui.mvvm_clean.domain.uses_cases.login_usecase.LoginUseCases
import com.base.hilt.ui.mvvm_clean.presentation.login.stateHandler.LoginStateHandler
import com.base.hilt.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(val repository: UserRepository,private val loginUseCases: LoginUseCases) :
    ViewModelBase() {

    //This is made before dont know flow
    val loginLiveData = SingleLiveEvent<ResponseHandler<ApolloResponse<LoginMutation.Data>>?>()

    fun loginApi(loginReq: LoginInput) {
        Log.i("2181", "loginApi: $loginReq")
        viewModelScope.launch {
            loginLiveData.postValue(ResponseHandler.Loading)
            val result = repository.onLoginApi(loginReq)
            loginLiveData.postValue(result)
        }
    }


    private val _userLoginData = MutableStateFlow(LoginStateHandler())
    val userLoginData: StateFlow<LoginStateHandler> = _userLoginData

    fun callLoginWithFlow(body: LoginRequestBody) {
        viewModelScope.launch {
            loginUseCases.invoke(body)
                .onEach { result ->
                    when (result) {
                        is FlowResponseHandler.OnSuccessResponse -> {
                            _userLoginData.value = LoginStateHandler(data = result.response)
                        }
                        is FlowResponseHandler.Loading -> {
                            _userLoginData.value = LoginStateHandler(isLoading = true)
                        }
                        is FlowResponseHandler.OnFailed -> {
                            _userLoginData.value = LoginStateHandler(
                                error = result.message ?: "An unexpected error occurred"
                            )
                        }
                    }
                }.launchIn(viewModelScope)
        }
    }

}