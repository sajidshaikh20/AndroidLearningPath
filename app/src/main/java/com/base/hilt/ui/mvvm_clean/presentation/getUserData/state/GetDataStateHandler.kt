package com.base.hilt.ui.mvvm_clean.presentation.getUserData.state

import com.base.hilt.ui.mvvm_clean.data.getUserData.GetUserData

data class GetDataStateHandler(
    val isLoading: Boolean = false,
    val data: GetUserData? = null,
    val error: String = ""
)