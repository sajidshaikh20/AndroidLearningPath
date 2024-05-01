package com.base.hilt.ui.mvvm_clean.presentation.login.handler

import android.widget.Toast
import com.base.hilt.ui.mvvm_clean.presentation.login.LoginFragment
import com.base.hilt.ui.mvvm_clean.presentation.login.validator.LoginWithCleanValidator

class LoginHandler(private val context: LoginFragment) {


    fun loginBtnClick(loginWithCleanValidator: LoginWithCleanValidator) {
        context.let {
            context.apply {
                Toast.makeText(requireContext(), "loginbtn press", Toast.LENGTH_SHORT).show()
                if (loginWithCleanValidator.isFormValidated(this)) {
                    loginBtn()
                }
            }
        }
    }



    fun loginwithGraphql() {
        context.let {
            context.apply {
                loginGraphql()
            }
        }
    }

    fun googleBtnClick() {
        context.let {
            context.apply {
                googleLogin()
            }
        }
    }

    fun googleNativeBtnClcik() {
        context.let {
            context.apply {
                googleNativeAuth()
            }
        }
    }

    fun facebookNativebtnCLick() {
        context.let {
            context.apply {
                facebookNativeAuth()
            }
        }
    }
}