package com.base.hilt.ui.home


import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.ApolloResponse
import com.base.hilt.ChallengeListQuery
import com.base.hilt.LogoutMutation
import com.base.hilt.base.ViewModelBase
import com.base.hilt.di.UserRepository
import com.base.hilt.network.ResponseHandler
import com.base.hilt.type.ChallengeListInput
import com.base.hilt.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val repository: UserRepository) :
    ViewModelBase() {

    val challengeListLiveData = SingleLiveEvent<ResponseHandler<ApolloResponse<ChallengeListQuery.Data>>?>()

    val logoutLiveData = SingleLiveEvent<ResponseHandler<ApolloResponse<LogoutMutation.Data>>>()



    fun challengeListApiCall(input: ChallengeListInput){
        viewModelScope.launch {
            challengeListLiveData.postValue(ResponseHandler.Loading)
            val response = repository.challengeListApi(input)
            challengeListLiveData.postValue(response)
        }
    }
    fun logoutapiCall(){
        viewModelScope.launch {
            logoutLiveData.postValue(ResponseHandler.Loading)
            val response = repository.logoutApi()
            logoutLiveData.postValue(response)
        }
    }
}

