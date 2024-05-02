package com.base.hilt.ui.mvvm_clean.presentation.login.validator


import com.base.hilt.R
import com.base.hilt.ui.mvvm_clean.presentation.login.LoginFragment
import com.base.hilt.utils.Validation

class LoginWithCleanValidator {

    /*
    LOGIN CREDENTIAL WITHOUT THIS
    *  var username: String = "correct_login@example.com"
       var password: String = "C0rr3Ct_P@55w0rd"
    * */

    var username: String = "correct_login@example.com"
    var password: String = "C0rr3Ct_P@55w0rd"

    fun isFormValidated(context: LoginFragment): Boolean {

        var isFormValidated = true

        if (username.trim().isEmpty()) {
            isFormValidated = false
            context.getDataBinding().tilUserName.error = "Please enter an email";
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            isFormValidated = false
            context.getDataBinding().tilUserName.error = "Please enter a valid email";
        } else {
            context.getDataBinding().tilUserName.error = null
        }

        //password validation
        if (password.trim().isBlank()) {
            isFormValidated = false
            context.getDataBinding().tilPassword.error =
                context.resources.getString(R.string.please_enter_password)
        } else if (password.trim().length < 8) {
            isFormValidated = false
            context.getDataBinding().tilPassword.error =
                context.resources.getString(R.string.please_enter_minimum_password)
        } else if (!Validation.isValidPassword(password.trim())) {
            isFormValidated = false
            context.getDataBinding().tilPassword.error =
                context.resources.getString(R.string.please_enter_valid_password)
        } else {
            context.getDataBinding().tilPassword.error = null
        }

        return isFormValidated
    }
}