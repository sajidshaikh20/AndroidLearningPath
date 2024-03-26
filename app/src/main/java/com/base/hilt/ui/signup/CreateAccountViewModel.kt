package com.base.hilt.ui.signup

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.ApolloResponse
import com.base.hilt.CreateAccountMutation
import com.base.hilt.VerifySmsOtpMutation
import com.base.hilt.base.ViewModelBase
import com.base.hilt.di.UserRepository
import com.base.hilt.network.ResponseHandler
import com.base.hilt.type.OtpInput
import com.base.hilt.type.SignUpInput
import com.base.hilt.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CreateAccountViewModel @Inject constructor(val repository: UserRepository) :
    ViewModelBase() {

    private val _signUpLiveData = SingleLiveEvent<ResponseHandler<ApolloResponse<CreateAccountMutation.Data>>>()
    val signUpLiveData: SingleLiveEvent<ResponseHandler<ApolloResponse<CreateAccountMutation.Data>>> = _signUpLiveData

    val otpVerifyLiveData = SingleLiveEvent<ResponseHandler<ApolloResponse<VerifySmsOtpMutation.Data>>>()

    fun signUpApi(signUpInput : SignUpInput){
        Log.i("2181", "signUpApi: $signUpInput")
        viewModelScope.launch {
            _signUpLiveData.postValue(ResponseHandler.Loading)
            val result =  repository.onSignUpApi(signUpInput)
            _signUpLiveData.postValue(result)
        }
    }
    fun otpVerifyApi(input : OtpInput){
        Log.i("2181", "loginApi: vm called")
        viewModelScope.launch {
            otpVerifyLiveData.postValue(ResponseHandler.Loading)
            val result =  repository.verifyOtp(input)
            otpVerifyLiveData.postValue(result)
        }
    }
}