package com.base.hilt.ui.getData.presentation

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.base.hilt.R
import com.base.hilt.base.FragmentBase
import com.base.hilt.base.ToolbarModel
import com.base.hilt.databinding.FragmentGetDataBinding
import com.base.hilt.network.ResponseHandler
import com.base.hilt.ui.getData.data.getUserData.GetUserData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GetDataFragment : FragmentBase<GetDataViewModel,FragmentGetDataBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_get_data
    }

    override fun setupToolbar() {
        viewModel.setToolbarItems(ToolbarModel(true, "Get Data", isBottomNavVisible = false, isSetting = false))
    }

    override fun initializeScreenVariables() {
        viewModel.calluserGetDataAPI()
        observeData()
    }
    private fun observeData() {
        viewModel.apply {
            userGetData.observe(this@GetDataFragment, Observer {
                if (it == null) {
                    return@Observer
                }
                when (it) {
                    is ResponseHandler.Loading -> {
                        viewModel.showProgressBar(true)
                    }
                    is ResponseHandler.OnFailed -> {
                        // Handle failure state
                        viewModel.showProgressBar(false)
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    is ResponseHandler.OnSuccessResponse<GetUserData?> -> {
                        viewModel.showProgressBar(false)
                        getDataBinding().model = it.response
                        Log.i("GetData", "observeData: ${it.response}")
                    }
                }
            })
        }
    }

    override fun getViewModelClass(): Class<GetDataViewModel> = GetDataViewModel::class.java

}