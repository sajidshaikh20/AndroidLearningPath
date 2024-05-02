package com.base.hilt.ui.mvvm_clean.presentation.login.handler

import android.widget.Toast
import com.base.hilt.ui.mvvm_clean.presentation.login.LoginFragment
import com.base.hilt.ui.mvvm_clean.presentation.login.validator.LoginWithCleanValidator

class LoginHandler(private val mcontext: LoginFragment) {

    fun loginBtnClick(loginWithCleanValidator: LoginWithCleanValidator) {
        mcontext.let {
            mcontext.apply {
                Toast.makeText(requireContext(), "loginbtn press", Toast.LENGTH_SHORT).show()
                if (loginWithCleanValidator.isFormValidated(mcontext)) {
                    loginBtn()
                }
            }
        }
    }


    fun loginwithGraphql() {
        mcontext.let {
            mcontext.apply {
                loginGraphql()
            }
        }
    }

    fun googleBtnClick() {
        mcontext.let {
            mcontext.apply {
                googleLogin()
            }
        }
    }

    fun googleNativeBtnClcik() {
        mcontext.let {
            mcontext.apply {
                googleNativeAuth()
            }
        }
    }

    fun facebookNativebtnCLick() {
        mcontext.let {
            mcontext.apply {
                facebookNativeAuth()
            }
        }
    }
}