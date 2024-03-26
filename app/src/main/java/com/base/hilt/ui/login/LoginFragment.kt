package com.base.hilt.ui.login

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.apollographql.apollo3.api.Optional
import com.base.hilt.R
import com.base.hilt.base.FragmentBase
import com.base.hilt.base.ToolbarModel
import com.base.hilt.databinding.FragmentLoginBinding
import com.base.hilt.network.ResponseHandler
import com.base.hilt.type.LoginInput
import com.base.hilt.utils.MyPreference
import com.base.hilt.utils.PrefKey
import com.base.hilt.utils.Validation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : FragmentBase<LoginViewModel, FragmentLoginBinding>() {

    @Inject
    lateinit var pref: MyPreference
    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun setupToolbar() {
        viewModel.setToolbarItems(ToolbarModel(false, "", false))
    }

    override fun getViewModelClass(): Class<LoginViewModel> = LoginViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun initializeScreenVariables() {
        observeData()
        getDataBinding().apply {
            createAccount.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToCreateAccountFragment()
                findNavController().navigate(action)
            }
            }

        }


    private fun observeData() {
        getDataBinding().btnLogin.setOnClickListener {
            if (checkValidations()){

                viewModel.loginApi(
                    LoginInput(
                    phone = "+1".plus(
                        getDataBinding().etUserName.text.toString().trim()
                            .filter { it.isDigit() }),
                    password = getDataBinding().etPassword.text.toString().trim(),
                    device_id = Optional.Present(""),
                    device_type = Optional.Present(""),
                    ip_address = Optional.Present(""),
                    user_timezone = Optional.Present(""),
                    )
                )
            }
        }
        viewModel.loginLiveData.observe(this) {
            viewModel.showProgressBar(it is ResponseHandler.Loading)
            when (it) {
                is ResponseHandler.OnFailed -> {
                    Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                }
                is ResponseHandler.OnSuccessResponse -> {
                    it.response.data?.login?.data?.access_token?.let { it1 ->
                        Log.i("2181", "observeData: $it1")
                        pref.setValueString(PrefKey.TOKEN,
                            it1
                        )
                    }
                    findNavController().navigate(R.id.action_Login_to_navigation_home)
                }
            }
        }

    }
    private fun checkValidations(): Boolean {
        var isValidForm = true
        when {
            getDataBinding().etUserName.text.toString().trim().isEmpty() -> {
                isValidForm = false
            }

            Validation.validatePhone(getDataBinding().etUserName.text.toString().trim()) -> {
                isValidForm = false

            }

            getDataBinding().etPassword.text.toString().trim().isEmpty() -> {
                isValidForm = false
            }

            !Validation.isValidPassword(getDataBinding().etPassword.text.toString()) -> {
                isValidForm = false
            }
        }
        return isValidForm
    }

    }

