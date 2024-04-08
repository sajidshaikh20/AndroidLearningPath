package com.base.hilt.ui.userForm.handler

import com.base.hilt.ui.userForm.UserFormFragment

class UserFormHandler(private val context: UserFormFragment) {
    fun resetBtnClick(){
        context.let {
            context.apply {
                resetData()
            }
        }
    }
    fun submitBtnClick(){
        context.let {
            context.apply {
                onSubmitForm()
            }
        }
    }
}