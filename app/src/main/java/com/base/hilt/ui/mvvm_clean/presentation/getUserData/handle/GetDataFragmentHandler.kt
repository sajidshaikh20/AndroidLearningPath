package com.base.hilt.ui.mvvm_clean.presentation.getUserData.handle

import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.base.hilt.ui.mvvm_clean.presentation.getUserData.GetDataFragment
import com.base.hilt.utils.PrefKey


class GetDataFragmentHandler(private val context: GetDataFragment) {

    fun getProductBtnOnClick() {
        context.let {
            context.apply {
                Toast.makeText(requireContext(), "GetProduct", Toast.LENGTH_SHORT).show()
                viewModel.callCartsApi()

            }
        }
    }

    fun logoutBtnOnclick() {
        context.let {
            context.apply {
                Toast.makeText(requireContext(), "Logout", Toast.LENGTH_SHORT).show()
                mPref.setValueBoolean(PrefKey.IS_USERlOGIN, false)
            }
        }
    }

}