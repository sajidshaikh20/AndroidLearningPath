package com.base.hilt.ui.notifications.handler

import com.base.hilt.ui.notifications.NotificationsFragment

class NotificationsFragmentClickHandler(private val context: NotificationsFragment) {

    fun getData(){
        context.let {
            context.apply {
                mobileData.clear()
                viewModel.callMobileDataList()
            }
        }
    }
    fun getFilterData(){
        context.let {
            context.apply {
                mobileData.clear()
                viewModel.callMobileDataListFilter()
            }
        }
    }
}