package com.base.hilt.ui.notifications

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.hilt.base.ViewModelBase
import com.base.hilt.network.HttpErrorCode
import com.base.hilt.network.ResponseData
import com.base.hilt.network.ResponseHandler1
import com.base.hilt.ui.notifications.model.MobileData
import com.base.hilt.ui.notifications.model.MobileData.MobileDataItem
import com.base.hilt.ui.notifications.repository.NotificationRepository
import com.base.hilt.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(var repository: NotificationRepository) : ViewModelBase() {

    private val _responseMobileDataList = MutableStateFlow<MobileDataItem?>(null)
    val responseMobileDataList: StateFlow<MobileDataItem?> = _responseMobileDataList

   /* private var _responseMobileDataList1 =
        MutableLiveData<ResponseHandler1<ResponseData<List<Any>>?>>()*/

    fun callMobileDataList(){
        viewModelScope.launch {
            repository.getMobileData()
                .collect { res ->
                    _responseMobileDataList.value = res
                  //  _responseMobileDataList.emit(res)
                }
        }
    }

    fun callMobileDataList1(){
        viewModelScope.launch {
            repository.getMobileData1().collect{
              //  _responseMobileDataList1.value = it
        }
        }
    }
}