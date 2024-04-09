package com.base.hilt.ui.userForm.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.base.hilt.base.ViewModelBase
import com.base.hilt.di.UserRepository
import com.base.hilt.ui.userForm.model.UserData
import com.base.hilt.ui.userForm.repository.UserFormRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserFormViewModel @Inject constructor(val repository: UserFormRepository) :
    ViewModelBase() {

    private val _userDataList = MutableStateFlow<UserData?>(null)
    val userDataList: StateFlow<UserData?> = _userDataList

     fun setUserInput(model: UserData) {
        Log.i("UserFormViewModel", "setUserInput: $model")
        viewModelScope.launch {
            repository.submitUserData(model)
        }
    }

    fun collectData() {
        // Submit user data to the repository
        viewModelScope.launch {
            repository.getUserData().collect {
                _userDataList.emit(it)
            }
        }
    }
}