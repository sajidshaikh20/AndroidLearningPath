package com.base.hilt.ui.home

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.base.hilt.FeedResultQuery
import com.base.hilt.R
import com.base.hilt.base.FragmentBase
import com.base.hilt.base.ToolbarModel
import com.base.hilt.databinding.FragmentHomeBinding
import com.base.hilt.network.ResponseData
import com.base.hilt.network.ResponseHandler
import com.base.hilt.ui.home.adapter.FeedResultAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : FragmentBase<HomeViewModel, FragmentHomeBinding>() {

   // var data: ArrayList<FeedResultQuery.Characters?> = arrayListOf()

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun setupToolbar() {
        viewModel.setToolbarItems(ToolbarModel(true, "Home", true))
    }

    override fun getViewModelClass(): Class<HomeViewModel> = HomeViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun initializeScreenVariables() {
        viewModel.onFeedResultApi()
        observeData()
    }

    private fun observeData() {
        viewModel.apply {

            onFeedResultData.observe(viewLifecycleOwner){ it ->
                when(it){
                    ResponseHandler.Loading->{
                        viewModel.showProgressBar(true)
                    }
                    is ResponseHandler.OnFailed->{
                        viewModel.showProgressBar(false)
                    }
                    is ResponseHandler.OnSuccessResponse -> {
                        viewModel.showProgressBar(false)

                        Log.i("2181", "observeData: $it")
                        val data = it.response.data
                        val characters: List<Character> = data?.characters?.results?.mapNotNull { characterResult ->
                            characterResult?.let {it1->
                                Character(
                                    it1.id,
                                    it1.name,
                                    it1.species,
                                    it1.gender,
                                    it1.image,
                                    it1.type
                                )
                            }
                        } ?: emptyList()
                        val characterArrayList = ArrayList(characters)
                        addRecycleViewData(characterArrayList)
                    }
                }
            }
        }



    }

    private fun addRecycleViewData(feed: ArrayList<Character>) {

        val adapter = FeedResultAdapter(requireContext(), feed)
        getDataBinding().rcvfeeds.adapter = adapter


    }
}



data class Character(
    val id: String? = null,
    val name: String?= null,
    val species: String?= null,
    val gender: String?= null,
    val image: String?= null,
    val type: String?= null
)