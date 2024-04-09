package com.base.hilt.ui.home

import android.util.Log
import android.view.View
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : FragmentBase<HomeViewModel, FragmentHomeBinding>() {

    var ChallengesList: ArrayList<Challenges?> = arrayListOf()

   val challengesObject = object : TypeToken<List<Challenges>>() {}.type
    lateinit var adapter: challengesAdapter
    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun setupToolbar() {
        viewModel.setToolbarItems(ToolbarModel(true, "Home", true))
    }

    override fun getViewModelClass(): Class<HomeViewModel> = HomeViewModel::class.java


    override fun initializeScreenVariables() {
        getDataBinding().handler = HomeHandler(this)
        observeData()
        adapter = challengesAdapter(requireContext(), ChallengesList)
        getDataBinding().rcvActiveChalengesList.adapter = adapter
    }
    private fun observeData() {

        viewModel.challengeListApiCall(
            ChallengeListInput(
                first = Optional.Present(10),
                page = Optional.Present(2),
                type = Optional.Present("past")
            )
        )

        viewModel.challengeListLiveData.observe(viewLifecycleOwner) { it ->
            when (it) {
                is  ResponseHandler.Loading-> {
                    viewModel.showProgressBar(false)
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
                }
            }
        }
    }
