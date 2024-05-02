package com.base.hilt.ui.userForm.validator

import com.base.hilt.ui.userForm.UserFormFragment

class UserFormValidator {

    var username: String = ""
    var fullname: String = ""
    var isMaleChecked: Boolean = false
    var isFemaleChecked: Boolean = false
    var isGenderChecked: Boolean = false

    fun isFormValidated(context: UserFormFragment): Boolean {
        var isFormValidated = true

        // Validate username
        if (username.trim().isEmpty()) {
            context.getDataBinding().tilUserName.error = "Please enter user Name"
            context.getDataBinding().tilUserName.requestFocus()
            isFormValidated = false
        } else if (username.trim().length < 6) {
            context.getDataBinding().tilUserName.error = "Minimum character 6"
            context.getDataBinding().tilUserName.requestFocus()
            isFormValidated = false
        } else {
            context.getDataBinding().tilUserName.error = null
        }

        // Validate fullname
        if (fullname.trim().isBlank()) {
            context.getDataBinding().tilFullName.error = "please Enter Full Name"
            if (username.isEmpty() || username.trim().length < 6) {
                context.getDataBinding().tilUserName.requestFocus()
            } else {
                context.getDataBinding().tilFullName.requestFocus()
            }
            isFormValidated = false
        } else {
            context.getDataBinding().tilFullName.error = null
        }
        isGenderChecked = context.getDataBinding().rbMale.isChecked || context.getDataBinding().rbFemale.isChecked
        if (!isGenderChecked){
            isFormValidated = false
           context.showToastMessage("Please Select  gender")
        }

        // Validate gender
//        isMaleChecked = context.getDataBinding().rbMale.isChecked
//        isFemaleChecked = context.getDataBinding().rbFemale.isChecked
//        if (!isMaleChecked && !isFemaleChecked) {
//            isFormValidated = false
//            context.showToastMessage("Please Select  gender")
//        }
        // Validate health issue
        if (!context.getDataBinding().checkBoxFever.isChecked &&
            !context.getDataBinding().checkBoxMaleria.isChecked &&
            !context.getDataBinding().checkBoxother.isChecked
        ) {
            context.showToastMessage("Please Select Health Issue")
            isFormValidated = false
        }
        if (context.getDataBinding().spinner.selectedItemPosition  == 0) {
            context.showToastMessage("Please Select an item from the spinner")
            isFormValidated = false
        }
        return isFormValidated
    }
}
