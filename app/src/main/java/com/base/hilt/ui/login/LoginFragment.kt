package com.base.hilt.ui.login

import android.os.Bundle
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.base.hilt.R
import com.base.hilt.base.FragmentBase
import com.base.hilt.base.ToolbarModel
import com.base.hilt.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : FragmentBase<LoginViewModel, FragmentLoginBinding>() {

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
            btnLogin.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginToNavigationHome()
                findNavController().navigate(action)
            }
            createAccount.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToCreateAccountFragment()
                findNavController().navigate(action)
            }
            }

        }


    private fun observeData() {

    }

    }

