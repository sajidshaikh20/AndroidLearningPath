package com.base.hilt.ui.userForm

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
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
import com.base.hilt.utils.MyPreference
import com.base.hilt.utils.PrefKey
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class UserFormFragment : FragmentBase<UserFormViewModel, FragmentUserFormBinding>() {

    @Inject
    lateinit var mPref: MyPreference
    override fun getLayoutId(): Int {
        return R.layout.fragment_user_form
    }

    override fun setupToolbar() {
        viewModel.setToolbarItems(ToolbarModel(false, "", false))
    }

    override fun initializeScreenVariables() {
        observeData()

        setupErrorHandling()
        //viewmodel collecting data
        viewModel.collectData()

        getDataBinding().apply {
            handler = UserFormHandler(this@UserFormFragment)
            validator = UserFormValidator()
        }
        setupSpinner()
        //get pref Data
        getPrefData()

    }

    private fun observeData() {

        lifecycleScope.launch {
            try {
                viewModel.userDataList
                    .collect { data ->
                        // This block will execute whenever new data is emitted
                        if (data != null) {
                            Log.i("UserFormFragment", "observeData: $data")
                            getDataBinding().clUserInfor.visibility = View.VISIBLE
                            setData(data)
                        } else {
                            getDataBinding().clUserInfor.visibility = View.GONE
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
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.spinner_items)
        )
        getDataBinding().spinner.adapter = adapter
    }


    fun onSubmitForm() {

        viewModel.setUserInput(
            UserData(
                userName = getDataBinding().validator?.username ?: "",
                fullName = getDataBinding().validator?.fullname?:  "",
                gender = if (getDataBinding().rbMale.isChecked) {
                    "Male"
                } else if (getDataBinding().rbFemale.isChecked) {
                    "Female"
                } else {
                    ""
                    //if user not select
                },
                healthIssue = getHealthIssue(),
                ageBetween = getDataBinding().spinner.selectedItemPosition.toString()
            )
        )

        viewModel.collectData()
    }


    private fun getHealthIssue(): String {
        var checkbox = ""
        if (getDataBinding().checkBoxFever.isChecked) {
            checkbox += getString(R.string.fever)
        }
        if (getDataBinding().checkBoxMaleria.isChecked) {
            checkbox += getString(R.string.malaria)
        }
        if (getDataBinding().checkBoxother.isChecked) {
            checkbox += getString(R.string.other)
        }
        return checkbox
    }

    private fun clearExitFormSelection() {
        getDataBinding().apply {
            edtUserName.text?.clear()
            edtFullName.text?.clear()
            edtUserName.clearFocus()
            edtFullName.clearFocus()
            rgGender.clearCheck()
            checkBoxFever.isChecked = false
            checkBoxother.isChecked = false
            checkBoxMaleria.isChecked = false
            spinner.setSelection(0)
        }
    }

    fun resetData() {
        mPref.setClearStringValue(PrefKey.USERMODEL)
        Toast.makeText(requireContext(), "Reset data ", Toast.LENGTH_SHORT).show()
        val json = mPref.getValueString(PrefKey.USERMODEL, "")
        val data = Gson().fromJson(json, UserData::class.java)
        if (data == null) {
            getDataBinding().clUserInfor.visibility = View.GONE
            clearExitFormSelection()
        }

    }

    fun showToastMessage(s: String) {
        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()
    }

    private fun setData(model: UserData) {
        getDataBinding().model = model

        getDataBinding().edtUserName.setText(model.userName)

        getDataBinding().edtFullName.setText(model.fullName)

        if (model.gender == "Male") {
            getDataBinding().validator?.isMaleChecked = true
        }
        if (model.gender == "Female") {
            getDataBinding().validator?.isFemaleChecked = true
        }
        if (model.healthIssue.contains(getString(R.string.fever))){
            getDataBinding().checkBoxFever.isChecked = true
        }
        if (model.healthIssue.contains(getString(R.string.malaria))){
            getDataBinding().checkBoxMaleria.isChecked = true
        }
        if (model.healthIssue.contains(getString(R.string.other))){
            getDataBinding().checkBoxother.isChecked = true
        }
        getDataBinding().spinner.setSelection(model.ageBetween.toInt())
    }

  private  fun setupErrorHandling() {
        getDataBinding().edtUserName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                getDataBinding().tilUserName.error = null
            }
        })

        getDataBinding().edtFullName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                getDataBinding().tilFullName.error = null
            }
        })
    }

    fun getPrefData(){
        val json = mPref.getValueString(PrefKey.USERMODEL, "")
        val data = Gson().fromJson(json, UserData::class.java)
        if (data != null) {
            getDataBinding().clUserInfor.visibility = View.VISIBLE
            setData(data)
        }
    }
}