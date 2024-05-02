package com.base.hilt.ui.userForm.handler

import com.base.hilt.ui.userForm.UserFormFragment
import com.base.hilt.ui.userForm.validator.UserFormValidator

class UserFormHandler(private val contextUser: UserFormFragment) {
    fun resetBtnClick(){
        contextUser.let {
            contextUser.apply {
                resetData()
            }
        }
    }
    fun submitBtnClick(userFormValidator: UserFormValidator){
        contextUser.let {
            contextUser.apply {
                if (userFormValidator.isFormValidated(contextUser)){
                    onSubmitForm()
                }
            }
        }
    }
}