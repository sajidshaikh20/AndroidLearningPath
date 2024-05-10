package com.base.hilt

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.base.hilt.base.ViewModelBase
import com.base.hilt.network.FlowResponseHandler
import com.base.hilt.ui.mvvm_clean.domain.uses_cases.get_data_profile.GetCartsUseCase
import com.base.hilt.ui.mvvm_clean.domain.uses_cases.get_data_profile.GetDataWithFlowUseCase
import com.base.hilt.ui.mvvm_clean.presentation.getUserData.state.GetCartsHandler
import com.base.hilt.ui.mvvm_clean.presentation.getUserData.state.GetDataStateHandler
import com.base.hilt.utils.MyPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainActivityViewModel @Inject constructor(private val getDataWithFlowUseCase: GetDataWithFlowUseCase) :
    ViewModelBase() {


    private var _isLoading = MutableStateFlow(true)
    var isLoading =
        _isLoading.asStateFlow() // It allows observing the loading state from outside the ViewModel.


       init {
           viewModelScope.launch {
               delay(3000)
               _isLoading.value = false
           }
       }

    private val _getData = MutableStateFlow(GetDataStateHandler())
    val getData: StateFlow<GetDataStateHandler> = _getData

    fun callUserDataWithFlow() {
        viewModelScope.launch {
            getDataWithFlowUseCase.invoke()
                .onEach { result ->
                    when (result) {
                        is FlowResponseHandler.OnSuccessResponse -> {

                            _isLoading.value = false

                            Log.i("MainActivityViewModel", ": onsucess ")
                            _getData.value = GetDataStateHandler(data = result.response)
                        }

                        is FlowResponseHandler.Loading -> {
                            _getData.value = GetDataStateHandler(isLoading = true)
                        }

                        is FlowResponseHandler.OnFailed -> {

                            _isLoading.value = false

                            Log.i("MainActivityViewModel", ": onfailed ")
                            _getData.value = GetDataStateHandler(
                                error = result.message ?: "An unexpected error occurred"
                            )
                        }
                    }
                }.launchIn(viewModelScope)
        }
    }


}