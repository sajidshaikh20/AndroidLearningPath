package com.base.hilt.ui.userForm.model

data class UserData(
    val userName: String,
    val fullName: String,
    val gender: String,
    val healthIssues: ArrayList<String>,
    val ageBetween: String,
)
