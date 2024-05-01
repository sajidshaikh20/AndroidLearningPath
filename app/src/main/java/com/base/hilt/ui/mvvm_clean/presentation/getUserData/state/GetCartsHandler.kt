package com.base.hilt.ui.mvvm_clean.presentation.getUserData.state

import com.base.hilt.ui.mvvm_clean.data.getUserData.Carts
data class GetCartsHandler(
    val isLoading: Boolean = false,
    val data: Carts? = null,
    val error: String = ""
)