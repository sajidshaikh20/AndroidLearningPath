package com.base.hilt.ui.home.handler

import android.widget.Toast
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
    fun crashBtnOnClick(){
        context.let {
            context.apply {
                Toast.makeText(requireContext(), "crash app", Toast.LENGTH_SHORT).show()
                 viewModel.logoutapiCall()
               // throw RuntimeException("Test Crash demo") // Force a crash
            }
        }
    }
    fun demoCleanBtnOnClick(){
        context.let {
            context.apply {
                Toast.makeText(requireContext(), "navigate new screen", Toast.LENGTH_SHORT).show()
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToGetUserFragment())
            }
        }
    }
}