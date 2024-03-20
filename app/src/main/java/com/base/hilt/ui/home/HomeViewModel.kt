package com.base.hilt.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.ApolloResponse
import com.base.hilt.FeedResultQuery
import com.base.hilt.base.ViewModelBase
import com.base.hilt.di.UserRepository
import com.base.hilt.network.ResponseData
import com.base.hilt.network.ResponseHandler
import com.base.hilt.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val repository: UserRepository) :
    ViewModelBase() {

    var onFeedResultData = SingleLiveEvent<ResponseHandler<ApolloResponse<FeedResultQuery.Data>>>()


    fun onFeedResultApi() {
        viewModelScope.launch {
            onFeedResultData.postValue(ResponseHandler.Loading)
            val res = repository.onFeedResultFetchApi()
            onFeedResultData.postValue(res)
        }
    }
}