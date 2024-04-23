package com.base.hilt.ui.login.handler

import com.base.hilt.ui.login.LoginFragment

class LoginHandler(private val context: LoginFragment) {

    fun googleBtnClick(){
        context.let {
            context.apply {
                googleLogin()
            }
        }
    }
    fun googleNativeBtnClcik(){
        context.let {
            context.apply {
                googleNativeAuth ()
            }
        }
    }
    fun facebookNativebtnCLick(){
        context.let {
            context.apply {
                facebookNativeAuth()
            }
        }
    }
}