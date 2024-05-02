package com.base.hilt.ui.notifications

import androidx.lifecycle.viewModelScope
import com.base.hilt.base.ViewModelBase

import com.base.hilt.ui.notifications.model.MobileDataItem
import com.base.hilt.ui.notifications.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(var repository: NotificationRepository) : ViewModelBase() {

    private val _responseMobileDataList = MutableStateFlow<MobileDataItem?>(null)
    val responseMobileDataList: StateFlow<MobileDataItem?> = _responseMobileDataList

    fun callMobileDataList(){
        viewModelScope.launch {
            repository.getMobileData()
                .collect { res ->
                    _responseMobileDataList.emit(res)
                }
        }
    }
    fun callMobileDataListFilter(){
        viewModelScope.launch {
            repository.getMobileData()
                .filter {
                    val price = it.data?.price ?: "0"
                    try {
                        price.toFloat() > 500
                    } catch (e: NumberFormatException) {
                        false
                    }
                }
                .collect { res ->
                    _responseMobileDataList.emit(res)
                }
        }
    }

}