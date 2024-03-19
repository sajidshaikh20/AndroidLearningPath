package com.base.hilt.ui.notifications

import android.content.Intent
import com.base.hilt.MainActivity
import com.base.hilt.R
import com.base.hilt.base.FragmentBase
import com.base.hilt.base.LocaleManager
import com.base.hilt.base.ToolbarModel
import com.base.hilt.databinding.FragmentNotificationsBinding
import com.base.hilt.utils.MyPreference
import com.base.hilt.utils.PrefKey
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationsFragment : FragmentBase<NotificationsViewModel, FragmentNotificationsBinding>() {

    @Inject
    lateinit var localeManager: LocaleManager

    @Inject
    lateinit var mPref: MyPreference
    override fun getLayoutId(): Int {
        return R.layout.fragment_notifications
    }

    override fun setupToolbar() {
        viewModel.setToolbarItems(ToolbarModel(true, "Change Language", true))
    }

    override fun initializeScreenVariables() {

        getDataBinding().viewModel = viewModel
        when (mPref.getValueString(
            PrefKey.SELECTED_LANGUAGE,
            PrefKey.EN_CODE
        )) {
            PrefKey.EN_CODE ->
                getDataBinding().btnEnglish.isChecked = true
            else ->
                getDataBinding().btnArabic.isChecked = true
        }
        observeData()
    }

    private fun observeData() {
        viewModel.chnageLanguageClick.observe(this, {
            if (getDataBinding().btnEnglish.isChecked) {
                mPref.setValueString(PrefKey.SELECTED_LANGUAGE, PrefKey.EN_CODE)
            } else {
                mPref.setValueString(PrefKey.SELECTED_LANGUAGE, PrefKey.AR_CODE)
            }
            localeManager.setNewLocale(
                activity as MainActivity,
                mPref.getValueString(PrefKey.SELECTED_LANGUAGE, PrefKey.AR_CODE).toString()
            )
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        })
    }

    override fun getViewModelClass(): Class<NotificationsViewModel> =
        NotificationsViewModel::class.java

}