package com.base.hilt.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import com.apollographql.apollo3.api.Optional
import com.base.hilt.R
import com.base.hilt.base.FragmentBase
import com.base.hilt.base.ToolbarModel
import com.base.hilt.databinding.FragmentHomeBinding
import com.base.hilt.network.ResponseHandler
import com.base.hilt.type.ChallengeListInput
import com.base.hilt.ui.home.adapter.challengesAdapter
import com.base.hilt.ui.home.handler.HomeHandler
import com.base.hilt.ui.model.Challenges
import com.base.hilt.utils.MyPreference
import com.base.hilt.utils.PrefKey
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : FragmentBase<HomeViewModel, FragmentHomeBinding>() {

     var ChallengesList: ArrayList<Challenges?> = arrayListOf()
   private val  PERMISSION_REQUEST_CODE = 112

   val challengesObject = object : TypeToken<List<Challenges>>() {}.type
    lateinit var adapter: challengesAdapter
    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun setupToolbar() {
        viewModel.setToolbarItems(ToolbarModel(true, "Home", isBottomNavVisible = true, isSetting = true))
    }

    override fun getViewModelClass(): Class<HomeViewModel> = HomeViewModel::class.java


    override fun initializeScreenVariables() {
        getDataBinding().handler = HomeHandler(this)
        getFCMToken()
        observeData()
        adapter = challengesAdapter(requireContext(), ChallengesList)
        getDataBinding().rcvActiveChalengesList.adapter = adapter
        //Ask for Permission in android 13
        if (Build.VERSION.SDK_INT > 32) {
            if (!shouldShowRequestPermissionRationale(PERMISSION_REQUEST_CODE.toString())){
                getNotificationPermission()
            }
        }
        val accessToken = AccessToken.getCurrentAccessToken()
        Log.i("homeFragment", "initializeScreenVariables: $accessToken")
        if (accessToken!=null){
            getUserDetails(accessToken)
        }

    }
    @Inject
    lateinit var pref: MyPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isUserLoggedIn = pref.getValueBoolean(PrefKey.IS_USERlOGIN, false)
        Log.d("SplashFragment", "Is user logged in: $isUserLoggedIn")
    }
    private fun observeData() {

        viewModel.challengeListApiCall(
            ChallengeListInput(
                first = Optional.Present(10),
                page = Optional.Present(2),
                type = Optional.Present("past")
            )
        )
//        viewModel.logoutapiCall()

        viewModel.challengeListLiveData.observe(viewLifecycleOwner) { it ->
            when (it) {
                is  ResponseHandler.Loading-> {
                    viewModel.showProgressBar(true)
                }
                is ResponseHandler.OnFailed -> {
                    viewModel.showProgressBar(false)
                }
                is ResponseHandler.OnSuccessResponse -> {
                    viewModel.showProgressBar(false)
                    val response = it.response.data?.challengeList?.data
                    Log.i("2181", "observeData: $response")
                    response.let {
                        if (it.isNullOrEmpty()){
                            getDataBinding().rcvActiveChalengesList.visibility = View.GONE
                        }else{
                            getDataBinding().rcvActiveChalengesList.visibility = View.VISIBLE
                            val myObjectList: List<Challenges> =
                            Gson().fromJson(Gson().toJson(it), challengesObject)
                            ChallengesList.clear()
                            ChallengesList.addAll(myObjectList)
                            adapter.updatedata()
                        }
                    }
                }

                else -> {}
            }
            }
        viewModel.logoutLiveData.observe(viewLifecycleOwner){
            when (it) {
                is  ResponseHandler.Loading-> {
                    viewModel.showProgressBar(true)
                }
                is ResponseHandler.OnFailed -> {
                    viewModel.showProgressBar(false)
                }
                is ResponseHandler.OnSuccessResponse -> {
                    viewModel.showProgressBar(false)
                    Log.i("Home", "observeData: ${it.response.data}")
                    Log.i("Home", "logout: ${it.response.data?.logout?.meta?.message_code}")
                }
                else -> {}
            }
        }
        }
    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@addOnCompleteListener
                }
                val token = task.result
                Log.i("Token", "getFCMToken: $token")
            }
    }
    private fun getNotificationPermission() {
        try {
            if (Build.VERSION.SDK_INT > 32) {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    PERMISSION_REQUEST_CODE
                )
            }
        } catch (e: Exception) {
            Log.d("error", "getNotificationPermission: $e")
        }
    }
    private fun getUserDetails(accessToken: AccessToken) {
        val request = GraphRequest.newMeRequest(
            accessToken
        ) { jsonObject, _ ->
            if (jsonObject != null) {
                val email = jsonObject.optString("email")
                val name = jsonObject.optString("name")
                val profilePicUrl = jsonObject.optJSONObject("picture")?.optJSONObject("data")?.optString("url")

                Log.i("facebook", "Email: $email, Name: $name")
                Log.i("facebook", "$profilePicUrl")
            } else {
                Log.e("facebook", "Unable to retrieve user details")
            }
        }
        val parameters = Bundle()
        parameters.putString("fields", "email,name,picture.type(large)")
        request.parameters = parameters
        request.executeAsync()
    }
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    // allow
                    Log.d("body", "granted")
                } else {
                    //deny
                    Log.d("permission","denied")
                }
                return
            }
        }
    }
    }
