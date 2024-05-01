package com.base.hilt.ui.notifications


import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.base.hilt.R
import com.base.hilt.base.FragmentBase
import com.base.hilt.base.LocaleManager
import com.base.hilt.base.ToolbarModel
import com.base.hilt.databinding.FragmentNotificationsBinding
import com.base.hilt.ui.notifications.handler.NotificationsFragmentClickHandler
import com.base.hilt.ui.notifications.model.MobileDataItem
import com.base.hilt.utils.MyPreference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@AndroidEntryPoint
class NotificationsFragment : FragmentBase<NotificationsViewModel, FragmentNotificationsBinding>() {

    @Inject
    lateinit var localeManager: LocaleManager
    var mobileData: ArrayList<MobileDataItem?> = arrayListOf()

    lateinit var adapter: MobileDataAdapter

    @Inject
    lateinit var mPref: MyPreference

    private lateinit var job: Job
    override fun getLayoutId(): Int {
        return R.layout.fragment_notifications
    }

    override fun setupToolbar() {
        viewModel.setToolbarItems(ToolbarModel(true, "Change Language", true))
    }

    override fun initializeScreenVariables() {
        getDataBinding().handler = NotificationsFragmentClickHandler(this)

        setUpCallListeners()
        viewModel.callMobileDataList()


        observeData()
        adapter = MobileDataAdapter(requireContext(), mobileData)
        getDataBinding().rcvmobile.adapter = adapter

    }

    private fun observeData() {

        Log.i("NotificationsFragment", "observeData: ")
        job = lifecycleScope.launchWhenStarted {
            try {
                viewModel.responseMobileDataList.onStart {
                    Log.i("NotificationsViewModel", "Onstart: ")
                }.onCompletion {
                    Log.i("NotificationsViewModel", "oncomplete}")
                }.collect {
                    Log.i("NotificationsViewModel", "collect: $it")
                    if (it != null) {
                        Log.i("NotificationsViewModel", "collect: $it")
                        mobileData.add(it)
                        adapter.updatedata()
                    } else {
                        println("Received null item")
                    }
                }
            } catch (e: Exception) {
                // Log any exceptions that occur during collection
                Log.i("exceptions", "observeData: $e")

            }
        }
    }

    private fun setUpCallListeners() {

        getDataBinding().btGetFilterData.setOnClickListener {
            mobileData.clear()
            viewModel.callMobileDataListFilter()
        }
    }

    override fun getViewModelClass(): Class<NotificationsViewModel> =
        NotificationsViewModel::class.java

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancel()
    }
}