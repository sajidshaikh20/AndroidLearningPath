package com.base.hilt.ui.notifications


import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.base.hilt.R
import com.base.hilt.base.FragmentBase
import com.base.hilt.base.LocaleManager
import com.base.hilt.base.ToolbarModel
import com.base.hilt.databinding.FragmentNotificationsBinding
import com.base.hilt.network.ResponseHandler1
import com.base.hilt.ui.notifications.model.MobileData
import com.base.hilt.utils.MyPreference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@AndroidEntryPoint
class NotificationsFragment : FragmentBase<NotificationsViewModel, FragmentNotificationsBinding>() {

    @Inject
    lateinit var localeManager: LocaleManager
    var mobileData: ArrayList<MobileData.MobileDataItem?> = arrayListOf()

    lateinit var adapter: MobileDataAdapter

    @Inject
    lateinit var mPref: MyPreference
    override fun getLayoutId(): Int {
        return R.layout.fragment_notifications
    }

    override fun setupToolbar() {
        viewModel.setToolbarItems(ToolbarModel(true, "Change Language", true))
    }

    override fun initializeScreenVariables() {
        viewModel.callMobileDataList()

        //flow api call with responce handler
        //     viewModel.callMobileDataList1()
        observeData()
        adapter = MobileDataAdapter(requireContext(), mobileData)
        getDataBinding().rcvmobile.adapter = adapter

    }

    private fun observeData() {

        // Assuming this is inside a Fragment or Activity
        lifecycleScope.launchWhenStarted {
            try {
                viewModel.responseMobileDataList.onStart {
                    Log.i("2181", "Onstart: ")
                }.onCompletion {
                        Log.i("2181", "oncomplete}")
                    }.collect {
                        if (it != null) {
                            mobileData.add(it)
                            adapter.updatedata()
                        } else {
                            println("Received null item")
                        }
                    }
            } catch (e: Exception) {
                // Log any exceptions that occur during collection
                Toast.makeText(requireContext(), "$e", Toast.LENGTH_SHORT).show()
            }

        }


        /* viewModel._responseMobileDataList1.observe(this@NotificationsFragment){
             when (it) {
                 is ResponseHandler1.OnSuccessResponse -> {
                     viewModel.showProgressBar(false)
                     Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show()
                 }
                 is ResponseHandler1.OnFailed -> {
                     viewModel.showProgressBar(false)
                     Toast.makeText(requireContext(), "onfailed", Toast.LENGTH_SHORT).show()
                 }
                 is ResponseHandler1.Loading -> {
                     viewModel.showProgressBar(true)
                     Toast.makeText(requireContext(), "loading", Toast.LENGTH_SHORT).show()
                 }
                 else -> {
                     viewModel.showProgressBar(false)
                 }
             }
         }*/


    }

    override fun getViewModelClass(): Class<NotificationsViewModel> =
        NotificationsViewModel::class.java
}