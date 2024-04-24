package com.base.hilt.ui.splash

import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.findNavController
import com.base.hilt.R
import com.base.hilt.base.FragmentBase
import com.base.hilt.base.ToolbarModel
import com.base.hilt.base.ViewModelBase
import com.base.hilt.databinding.FragmentSplashBinding
import com.base.hilt.databinding.GoogleButtonLayoutBinding
import com.base.hilt.utils.Constants
import com.base.hilt.utils.MyPreference
import com.base.hilt.utils.PrefKey
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class SplashFragment : FragmentBase<ViewModelBase, FragmentSplashBinding>() {

    @Inject
    lateinit var pref: MyPreference

    private var mPushType: String? = ""
    override fun getLayoutId(): Int {
        return R.layout.fragment_splash
    }

    override fun setupToolbar() {
        viewModel.setToolbarItems(ToolbarModel(false, null, false))
    }
    //private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun getViewModelClass(): Class<ViewModelBase> = ViewModelBase::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        mPushType = requireActivity().intent.getStringExtra(Constants.push_type)

        if (mPushType != null) {
            when (mPushType) {
                "User" -> {
                    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToUserForm())
                }
                else ->{
                    Log.i("onCreateSplash", "onCreate: failed ")
                }

            }
        }

        val token = pref.getValueString(PrefKey.TOKEN, "")
        Log.i("2181", "SPLAHS: $token")
    }

    override fun initializeScreenVariables() {

    }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch(context = Dispatchers.Main) {
            delay(3000)
            val account = GoogleSignIn.getLastSignedInAccount(requireContext())
            val isUserLoggedIn = pref.getValueBoolean(PrefKey.IS_USERlOGIN, false)
            Log.d("SplashFragment", "Is user logged in: $isUserLoggedIn")
            val accessToken = AccessToken.getCurrentAccessToken()
            if (isUserLoggedIn || account != null || accessToken!=null) {
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToNavigationHome())
            } else {
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLogin())
            }
        }
    }
}
