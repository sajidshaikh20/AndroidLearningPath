package com.base.hilt.ui.home

import android.os.Bundle
import androidx.lifecycle.Observer
import com.base.hilt.R
import com.base.hilt.base.FragmentBase
import com.base.hilt.base.ToolbarModel
import com.base.hilt.databinding.FragmentHomeBinding
import com.base.hilt.network.ResponseData
import com.base.hilt.network.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : FragmentBase<HomeViewModel, FragmentHomeBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun setupToolbar() {
        viewModel.setToolbarItems(ToolbarModel(true, "Home", true))
    }

    override fun getViewModelClass(): Class<HomeViewModel> = HomeViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.callApi()
    }

    override fun initializeScreenVariables() {
        observeData()
    }

    private fun observeData() {
        viewModel.responseLiveHomeVendorListResponse.observe(this, Observer {
            if (it == null) {
                return@Observer
            }
            when (it) {
                is ResponseHandler.Loading -> {
                    viewModel.showProgressBar(true)
                }
                is ResponseHandler.OnFailed -> {
                    viewModel.showProgressBar(false)
                    it.code?.let { it1 -> httpFailedHandler(it1, it.message, it.messageCode) }
                }
                is ResponseHandler.OnSuccessResponse<ResponseData<HomeScreenVendorsListResponse>?> -> {
                    viewModel.showProgressBar(false)
                    getDataBinding().model = it.response?.data
                }
            }
        })
    }
}