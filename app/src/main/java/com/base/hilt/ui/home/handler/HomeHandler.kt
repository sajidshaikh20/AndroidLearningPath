package com.base.hilt.ui.home.handler

import androidx.navigation.fragment.findNavController
import com.base.hilt.ui.home.HomeFragment
import com.base.hilt.ui.home.HomeFragmentDirections

class HomeHandler(private val context: HomeFragment) {
    fun formBtnOnClick() {
        context.let {
            context.apply {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToUserFromFragment())
            }
        }
    }
}