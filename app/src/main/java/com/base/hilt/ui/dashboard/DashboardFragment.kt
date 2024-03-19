package com.base.hilt.ui.dashboard

import com.base.hilt.R
import com.base.hilt.base.FragmentBase
import com.base.hilt.base.ToolbarModel
import com.base.hilt.bind.BindAdapters
import com.base.hilt.bottom_sheets.ProfileImageBottomSheetDialog
import com.base.hilt.databinding.FragmentDashboardBinding
import com.base.hilt.utils.DebugLog
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
        viewModel.editProfileClickEvent.observe(this, {
            val bottomSheetFragment = ProfileImageBottomSheetDialog(object :
                ProfileImageBottomSheetDialog.ItemClickListener {
                override fun itemClick(imagePath: String, isDeleteImage: Boolean) {
                    DebugLog.d("itemClick:$imagePath")
                    BindAdapters.bindImageData(
                        getDataBinding().imageViewEditProfile,
                        imagePath,
                        null
                    )
                }
            }, false)
            bottomSheetFragment.show(childFragmentManager, "Dialog")
        })
    }

    override fun getViewModelClass(): Class<DashboardViewModel> = DashboardViewModel::class.java

}