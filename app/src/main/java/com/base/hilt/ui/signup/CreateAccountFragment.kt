package com.base.hilt.ui.signup

import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.apollographql.apollo3.api.Optional
import com.base.hilt.R
import com.base.hilt.base.FragmentBase
import com.base.hilt.base.ToolbarModel
import com.base.hilt.databinding.FragmentCreateAccountBinding
import com.base.hilt.network.ResponseHandler
import com.base.hilt.type.OtpInput
import com.base.hilt.type.SignUpInput
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateAccountFragment : FragmentBase<CreateAccountViewModel, FragmentCreateAccountBinding>() {
    override fun getLayoutId(): Int {
        return  R.layout.fragment_create_account
    }

    override fun setupToolbar() {
        viewModel.setToolbarItems(ToolbarModel(false, "", false))
    }

    override fun initializeScreenVariables() {
        observeData()

    }

    override fun getViewModelClass(): Class<CreateAccountViewModel> =CreateAccountViewModel::class.java

    private fun observeData() {
        createAccount()
        viewModel.signUpLiveData.observe(viewLifecycleOwner) {
            when (it) {
                ResponseHandler.Loading -> {
                    viewModel.showProgressBar(true)
                }

                is ResponseHandler.OnFailed -> {
                    viewModel.showProgressBar(false)
                    Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                }

                is ResponseHandler.OnSuccessResponse -> {
                    viewModel.showProgressBar(false)
                    Log.i("2181", "observeData: OnSuccessResponse")
                    if (it.response.data!=null){
                        Log.i("2181", "observeData: ${it.response}")
                        val email = it.response.data?.signup?.data?.email
                        val mobile =it.response.data?.signup?.data?.mobile_number
                        val otp = it.response.data?.signup?.data?.otp
                        Log.i("2181", "otp:$otp")
                        val type = "1"
                        val uuid = it.response.data?.signup?.data?.uuid
                        val otpData= arrayOf(email,mobile,otp,type,uuid)
                        Log.i("2181", "observeData: $otpData")

                        viewModel.otpVerifyApi(
                            OtpInput(
                                otp = Optional.Present(otp),
                                type = Optional.Present(type),
                                uuid = Optional.Present(uuid)
                            )
                        )


                    }

                }
            }
        }
        viewModel.otpVerifyLiveData.observe(viewLifecycleOwner) {
            when (it) {
                ResponseHandler.Loading -> {
                    viewModel.showProgressBar(true)
                }

                is ResponseHandler.OnFailed -> {
                    viewModel.showProgressBar(false)
                    Log.i("2181", "observeData: ${it.message}")
                }

                is ResponseHandler.OnSuccessResponse -> {
                    Log.i("2181", "otpverify on success: ${it.response}")
                    viewModel.showProgressBar(false)
                    val action = CreateAccountFragmentDirections.actionCreateAccountFragmentToLoginFragment()
                    findNavController().navigate(action)
                }
            }
        }

    }
   fun createAccount(){
       getDataBinding().btnCreate.setOnClickListener {
           viewModel.signUpApi(
           SignUpInput(
               first_name = Optional.Present(
                   getDataBinding().etUserName.text.toString().trim()
               ),
               last_name = Optional.Present(
                   getDataBinding().etLastName.text.toString().trim()
               ),
               mobile_number = Optional.Present(
                   "+1" + getDataBinding().etMobile.text.toString()
                       .trim().filter { it.isDigit() }),
               alias = Optional.Present(
                   getDataBinding().etAlias.text.toString().trim()
               ),
               password = Optional.Present(
                   getDataBinding().etPassword.text.toString().trim()
               ),
               confirm_password = Optional.Present(
                   getDataBinding().etConfirmPassword.text.toString().trim()
               ),
               email = Optional.Present(
                   getDataBinding().etEmail.text.toString().trim()
               ),
               dob = Optional.Present(
                   "03-12-1999"
               ),
               referral_code = Optional.Present(
                   getDataBinding().etReferralCode.text.toString().trim()
               ),
               device_id = Optional.Present(""),
               device_type = Optional.Present("1"),
               ip_address = Optional.Present("192.168.1.45"),
               user_timezone = Optional.Present("Asia/Culcutta")
           )
           )
       }
   }

}