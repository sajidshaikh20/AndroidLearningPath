package com.base.hilt.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.credentials.GetCredentialRequest
import androidx.navigation.fragment.findNavController
import com.apollographql.apollo3.api.Optional
import com.base.hilt.R
import com.base.hilt.base.FragmentBase
import com.base.hilt.base.ToolbarModel
import com.base.hilt.databinding.FragmentLoginBinding
import com.base.hilt.network.ResponseHandler
import com.base.hilt.type.LoginInput
import com.base.hilt.ui.login.handler.LoginHandler
import com.base.hilt.utils.MyPreference
import com.base.hilt.utils.PrefKey
import com.base.hilt.utils.Validation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.Scope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : FragmentBase<LoginViewModel, FragmentLoginBinding>() {

    @Inject
    lateinit var pref: MyPreference

    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var auth: FirebaseAuth

    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun setupToolbar() {
        viewModel.setToolbarItems(ToolbarModel(false, "", false))
    }

    override fun getViewModelClass(): Class<LoginViewModel> = LoginViewModel::class.java
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }


    override fun initializeScreenVariables() {
        observeData()
        getDataBinding().apply {
            handler = LoginHandler(this@LoginFragment)
            createAccount.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToCreateAccountFragment()
                findNavController().navigate(action)
            }
        } }
    private fun observeData() {
        viewModel.loginLiveData.observe(this) {
            viewModel.showProgressBar(it is ResponseHandler.Loading)
            when (it) {
                is ResponseHandler.OnFailed -> {
                    Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                }
                is ResponseHandler.OnSuccessResponse -> {
                    if (it.response.data?.login?.data?.access_token != null) {
                        pref.setValueString(
                            PrefKey.TOKEN,
                            it.response.data?.login?.data?.access_token!!
                        )
                    }
                    val token = pref.getValueString(PrefKey.TOKEN, "")
                    pref.setValueBoolean(PrefKey.IS_USERlOGIN, true)
                    findNavController().navigate(R.id.action_Login_to_navigation_home)
                }
                else -> {}
            }
        }
        getDataBinding().btnLogin.setOnClickListener {
            if (checkValidations()) {
                Log.i("2181", "observeData: after success check validation")
                viewModel.loginApi(
                    LoginInput(
                        phone = getString(R.string.plus).plus(
                            getDataBinding().etUserName.text.toString().trim()
                                .filter { it.isDigit() }),
                        password = getDataBinding().etPassword.text.toString().trim(),
                        device_id = Optional.Present("asfasfasasf"),
                        device_type = Optional.Present("2"),
                        ip_address = Optional.Present(""),
                        user_timezone = Optional.Present("Asia/Calcutta"),
                    )
                )
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
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    viewModel.showProgressBar(true)
                    task.addOnCompleteListener { signInTask ->
                        if (signInTask.isSuccessful) {
                            viewModel.showProgressBar(false)
                            val account = signInTask.result
                            val email = account?.email
                            val displayName = account?.displayName
                            // You can handle the sign-in success here
                           // pref.setValueBoolean(PrefKey.IS_USERlOGIN, true)
                            Log.i("sign in", "1: ${account.familyName}")
                            Log.i("sign in", "2: $displayName")
                            Log.i("sign in", "3: ${account.photoUrl}")
                            Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT)
                                .show()
                            findNavController().navigate(R.id.action_Login_to_navigation_home)
                        } else {
                            // Handle sign-in failure
                            Toast.makeText(requireContext(), "Sign-in failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Failed to retrieve sign-in data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(requireContext(), "Failed to sign in", Toast.LENGTH_SHORT).show()
            }
        }

    fun googleNativeAuth() {
        Toast.makeText(requireContext(), "Google native", Toast.LENGTH_SHORT).show()
        signIn()
    }

    private fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestProfile()
            .requestScopes(Scope(Scopes.PLUS_ME))
            .requestScopes(Scope(Scopes.PLUS_LOGIN))
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        val signInClient = googleSignInClient.signInIntent
        launcher.launch(signInClient)
    }
    fun googleLogin() {
        Toast.makeText(requireContext(), "google ", Toast.LENGTH_SHORT).show()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestProfile()
            .requestScopes(Scope(Scopes.PLUS_ME))
            .requestScopes(Scope(Scopes.PLUS_LOGIN))
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        val signInClient = googleSignInClient.signInIntent
        laucher.launch(signInClient)
    }

    private val laucher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                viewModel.showProgressBar(true)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount? = task.result
                    val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
                            viewModel.showProgressBar(true)
                            val user = auth.currentUser
                            Log.i("Firebase", ":${user?.email} ")
                            pref.setValueBoolean(PrefKey.IS_USERlOGIN, true)
                            updateUI(user)
                            Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            viewModel.showProgressBar(true)
                            Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
            }
        }
    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Log.i("user", "updateUI: ${user.displayName}")
            Log.i("user", "updateUI: ${user.photoUrl}")
            findNavController().navigate(R.id.action_Login_to_navigation_home)
        }
    }
    fun facebookNativeAuth(){
        Toast.makeText(requireContext(), "Facebook", Toast.LENGTH_SHORT).show()
    }
}

