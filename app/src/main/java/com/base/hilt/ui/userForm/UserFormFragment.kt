package com.base.hilt.ui.userForm

import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.base.hilt.R
import com.base.hilt.base.FragmentBase
import com.base.hilt.base.ToolbarModel
import com.base.hilt.databinding.FragmentUserFormBinding
import com.base.hilt.ui.userForm.handler.UserFormHandler
import com.base.hilt.ui.userForm.model.UserData
import com.base.hilt.ui.userForm.validator.UserFormValidator
import com.base.hilt.ui.userForm.viewmodel.UserFormViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UserFormFragment : FragmentBase<UserFormViewModel, FragmentUserFormBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_user_form
    }

    override fun setupToolbar() {
        viewModel.setToolbarItems(ToolbarModel(false, "", false))
    }

    override fun initializeScreenVariables() {
        observeData()
        //viewmodel collecting data
        viewModel.collectData()

        getDataBinding().apply {
            handler = UserFormHandler(this@UserFormFragment)
            validator = UserFormValidator()
        }

        setupSpinner()

    }

    private fun observeData() {

        lifecycleScope.launch {
            try {
                viewModel.userDataList
                    .collect { data ->
                        // This block will execute whenever new data is emitted
                        if (data != null) {
                            Log.i("UserFormFragment", "observeData: $data")
                            getDataBinding().includeUserLayout.model = data
                        }
                    }
            } catch (e: Exception) {
                Log.e("UserFormFragment", "Exception in collect: ${e.message}", e)
            }
        }
    }

    override fun getViewModelClass(): Class<UserFormViewModel> = UserFormViewModel::class.java

    private fun setupSpinner() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("20 to 25", "25 to 30", "30 to 50")
        )
        getDataBinding().spinner.adapter = adapter
    }

    fun resetData() {
        Toast.makeText(requireContext(), "Reset data ", Toast.LENGTH_SHORT).show()
    }

    fun onSubmitForm() {
        viewModel.collectData()
        viewModel.setUserInput(
            UserData(
                userName = getDataBinding().edtUserName.text.toString().trim(),
                fullName = getDataBinding().edtFullName.text.toString().trim(),
                gender = if (getDataBinding().rbMale.isChecked) {
                    "Male"
                } else if (getDataBinding().rbFemale.isChecked) {
                    "Female"
                } else {
                    ""
                    //if user not select
                },
                healthIssue = getHealthIssue(),
                ageBetween = getDataBinding().spinner.selectedItem.toString()
            )
        )
    }
    private fun getHealthIssue(): String {
        var checkbox = ""
        if (getDataBinding().checkBoxFever.isChecked) {
            checkbox += "Fever "
        }
        if (getDataBinding().checkBoxMaleria.isChecked) {
            checkbox += "Maleria "
        }
        if (getDataBinding().checkBoxother.isChecked) {
            checkbox += "Other "
        }
        return checkbox
    }
}