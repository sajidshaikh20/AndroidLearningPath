package com.base.hilt.ui.mvvm_clean.data.getUserData


import com.fasterxml.jackson.annotation.JsonProperty

data class LoginRequestBody(
    @JsonProperty("login")
    val login: String,
    @JsonProperty("password")
    val password: String
)