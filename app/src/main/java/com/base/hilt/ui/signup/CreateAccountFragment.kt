package com.base.hilt.ui.signup

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.base.hilt.R
import com.base.hilt.base.FragmentBase
import com.base.hilt.base.ToolbarModel
import com.base.hilt.databinding.FragmentCreateAccountBinding
import com.base.hilt.network.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateAccountFragment : FragmentBase<CreateAccountViewModel, FragmentCreateAccountBinding>() {
    override fun getLayoutId(): Int {
        return  R.layout.fragment_create_account
    }

    override fun setupToolbar() {
        viewModel.setToolbarItems(ToolbarModel(false, "", false))
    }

    override fun initializeScreenVariables() {
        observeData()
    }

    override fun getViewModelClass(): Class<CreateAccountViewModel> =CreateAccountViewModel::class.java

    private fun observeData() {
        viewModel.signUpLiveData.observe(viewLifecycleOwner) {
            when (it) {
                ResponseHandler.Loading -> {
                    viewModel.showProgressBar(true)
                }

                is ResponseHandler.OnFailed -> {
                    viewModel.showProgressBar(false)
                }

                is ResponseHandler.OnSuccessResponse -> {
                    viewModel.showProgressBar(false)
                    if (it.response.data!=null){
                        val email = it.response.data?.signup?.data?.email
                        val mobile =it.response.data?.signup?.data?.mobile_number
                        val otp = it.response.data?.signup?.data?.otp
                        val type = "1"
                        val uuid = it.response.data?.signup?.data?.uuid

                        val otpData= arrayOf(email,mobile,otp,type,uuid)

                        Log.i("2181", "observeData: $otpData")
                        val action = CreateAccountFragmentDirections.actionCreateAccountFragmentToLoginFragment()
                        findNavController().navigate(action)
                    }

                }
            }
        }

    }

}