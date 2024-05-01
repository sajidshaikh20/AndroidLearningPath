package com.base.hilt.ui.mvvm_clean.presentation.login.stateHandler

import com.base.hilt.ui.mvvm_clean.data.getUserData.LoginResponceWithClean


data class LoginStateHandler(
    val isLoading: Boolean = false,
    val data: LoginResponceWithClean? = null,
    val error: String = ""
)