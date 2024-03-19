package com.base.hilt.ui.dashboard

import com.base.hilt.base.ViewModelBase
import com.base.hilt.utils.SingleLiveEvent

class DashboardViewModel : ViewModelBase() {

    var editProfileClickEvent = SingleLiveEvent<Any>()

    fun editPhotoClick() {
        editProfileClickEvent.call()
    }
}