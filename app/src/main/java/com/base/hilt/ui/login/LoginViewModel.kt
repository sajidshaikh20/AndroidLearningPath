package com.base.hilt.ui.login

import com.base.hilt.base.ViewModelBase
import com.base.hilt.di.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(val repository: UserRepository) :
    ViewModelBase() {


}