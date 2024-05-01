package com.base.hilt.ui.mvvm_clean.data.getUserData


import com.fasterxml.jackson.annotation.JsonProperty


data class LoginResponceWithClean(
    @JsonProperty("message")
    val message: String,
    @JsonProperty("status")
    val status: String
)