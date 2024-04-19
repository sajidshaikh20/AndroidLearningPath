package com.base.hilt.ui.splash

import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.findNavController
import com.base.hilt.R
import com.base.hilt.base.FragmentBase
import com.base.hilt.base.ToolbarModel
import com.base.hilt.base.ViewModelBase
import com.base.hilt.databinding.FragmentSplashBinding
import com.base.hilt.utils.Constants
import com.base.hilt.utils.MyPreference
import com.base.hilt.utils.PrefKey
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

    override fun getViewModelClass(): Class<ViewModelBase> = ViewModelBase::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            val isUserLoggedIn = pref.getValueBoolean(PrefKey.IS_USERlOGIN, false)
            Log.d("SplashFragment", "Is user logged in: " + isUserLoggedIn)

            if (isUserLoggedIn) {
                Log.i("SplashFragment", "if: isUserLoggedIn")
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToNavigationHome())
            } else {
                Log.i("SplashFragment", "else")
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLogin())
            }
        }
    }
}
