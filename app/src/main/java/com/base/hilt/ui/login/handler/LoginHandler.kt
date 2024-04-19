package com.base.hilt.ui.login.handler

import android.content.Context
import android.util.Log
import com.base.hilt.ui.login.LoginFragment


class LoginHandler(private val context: LoginFragment) {

    fun googleBtnClick(){
        context.let {
            context.apply {
                googleLogin()
            }
        }
    }
}