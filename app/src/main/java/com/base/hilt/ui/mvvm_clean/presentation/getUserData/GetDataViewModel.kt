package com.base.hilt.ui.mvvm_clean.presentation.getUserData


import androidx.lifecycle.viewModelScope
import com.base.hilt.base.ViewModelBase
import com.base.hilt.network.FlowResponseHandler
import com.base.hilt.ui.mvvm_clean.domain.uses_cases.get_data_profile.GetCartsUseCase
import com.base.hilt.ui.mvvm_clean.domain.uses_cases.get_data_profile.GetDataWithFlowUseCase
import com.base.hilt.ui.mvvm_clean.domain.uses_cases.get_data_profile.ProductDetailsUseCase
import com.base.hilt.ui.mvvm_clean.presentation.getUserData.state.GetCartsHandler
import com.base.hilt.ui.mvvm_clean.presentation.getUserData.state.GetDataStateHandler
import com.base.hilt.ui.mvvm_clean.presentation.getUserData.state.ProductDetailsStateHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetDataViewModel @Inject constructor(
    private val getCartsUseCase: GetCartsUseCase,
    private val productDetailsUseCase: ProductDetailsUseCase,
    private val getDataWithFlowUseCase: GetDataWithFlowUseCase
) : ViewModelBase() {


    //get user carts
    private val _userCartsData = MutableStateFlow(GetCartsHandler())
    val userCartsData: StateFlow<GetCartsHandler> = _userCartsData

    // get product details
    private val _productDetails = MutableStateFlow(ProductDetailsStateHandler())
    val productDetails: StateFlow<ProductDetailsStateHandler> = _productDetails


    private val _getData = MutableStateFlow(GetDataStateHandler())
    val getData: StateFlow<GetDataStateHandler> = _getData

    fun callUserDataWithFlow() {
        viewModelScope.launch {
            getDataWithFlowUseCase.invoke()
                .onEach { result ->
                    when (result) {
                        is FlowResponseHandler.OnSuccessResponse -> {
                            _getData.value = GetDataStateHandler(data = result.response)
                        }

                        is FlowResponseHandler.Loading -> {
                            _getData.value = GetDataStateHandler(isLoading = true)
                        }

                        is FlowResponseHandler.OnFailed -> {
                            _getData.value = GetDataStateHandler(
                                error = result.message ?: "An unexpected error occurred"
                            )
                        }
                    }
                }.launchIn(viewModelScope)
        }
    }

    fun callCartsApi() {
        viewModelScope.launch {

            getCartsUseCase.invoke()
                .onEach { result ->
                    when (result) {
                        is FlowResponseHandler.OnSuccessResponse -> {
                            _userCartsData.value = GetCartsHandler(data = result.response)
                        }

                        is FlowResponseHandler.Loading -> {
                            _userCartsData.value = GetCartsHandler(isLoading = true)
                        }

                        is FlowResponseHandler.OnFailed -> {
                            _userCartsData.value = GetCartsHandler(
                                error = result.message ?: "An unexpected error occurred"
                            )
                        }
                    }
                }.launchIn(viewModelScope)
        }
    }

    fun productDetailsCallAPI(id: Int) {
        viewModelScope.launch {
            productDetailsUseCase.invoke(id).onEach { result ->
                when (result) {
                    is FlowResponseHandler.OnSuccessResponse -> {
                        _productDetails.value = ProductDetailsStateHandler(data = result.response)
                    }

                    is FlowResponseHandler.Loading -> {
                        _productDetails.value = ProductDetailsStateHandler(isLoading = false)
                    }
                    is FlowResponseHandler.OnFailed -> {
                        _productDetails.value = ProductDetailsStateHandler(
                            error = result.message ?: "An unexpected error occurred"
                        )
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}