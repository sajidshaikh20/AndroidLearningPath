package com.base.hilt.ui.dashboard

import com.base.hilt.R
import com.base.hilt.base.FragmentBase
import com.base.hilt.base.ToolbarModel
import com.base.hilt.databinding.FragmentDashboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : FragmentBase<DashboardViewModel, FragmentDashboardBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_dashboard
    }

    override fun setupToolbar() {
        viewModel.setToolbarItems(ToolbarModel(true, "Dashboard", true))
    }

    override fun initializeScreenVariables() {

        getDataBinding().viewModel = viewModel
        observeData()
    }

    private fun observeData() {

    }

    override fun getViewModelClass(): Class<DashboardViewModel> = DashboardViewModel::class.java

}