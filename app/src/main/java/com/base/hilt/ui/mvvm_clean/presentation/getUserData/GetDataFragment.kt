package com.base.hilt.ui.mvvm_clean.presentation.getUserData


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.base.hilt.R
import com.base.hilt.base.FragmentBase
import com.base.hilt.base.ToolbarModel
import com.base.hilt.databinding.FragmentGetDataBinding
import com.base.hilt.ui.mvvm_clean.data.getUserData.ProductDetails
import com.base.hilt.ui.mvvm_clean.data.helper.ProductDetailsHelper
import com.base.hilt.ui.mvvm_clean.presentation.getUserData.adapter.ProductAdapter
import com.base.hilt.ui.mvvm_clean.presentation.getUserData.handle.GetDataFragmentHandler
import com.base.hilt.utils.MyPreference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class GetDataFragment : FragmentBase<GetDataViewModel, FragmentGetDataBinding>() {

    @Inject
    lateinit var mPref: MyPreference

    override fun getLayoutId(): Int {
        return R.layout.fragment_get_data
    }

    override fun setupToolbar() {
        viewModel.setToolbarItems(
            ToolbarModel(
                true,
                "Get Data",
                isBottomNavVisible = false,
                isSetting = false
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataBinding().lifecycleOwner = viewLifecycleOwner
    }


    override fun initializeScreenVariables() {
        // set handler
        getDataBinding().handler = GetDataFragmentHandler(this)

        viewModel.callUserDataWithFlow()
        collectUserData()
        //collectCartsData()
        collectCartsData()


    }

    override fun getViewModelClass(): Class<GetDataViewModel> = GetDataViewModel::class.java


    /* private fun showDialog(discription: String) {
         val dialogBinding: DialogBinding = DataBindingUtil.inflate(
             LayoutInflater.from(requireActivity()),
             R.layout.dialog,
             null,
             false
         )
         val dialog = Dialog(requireActivity())
         dialog.apply {
             requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY)
             setCancelable(false)
             setContentView(dialogBinding.root)
         }
         dialogBinding.discription = discription

         dialogBinding.btnYes.setOnClickListener {
             dialog.dismiss()
         }
         dialog.show()
     }*/

    private fun collectUserData() {
        lifecycleScope.launch {
            viewModel.getData.collect {
                if (it.isLoading) {
                    viewModel.showProgressBar(true)
                    Log.i("GetData", "Loading")
                } else if (it.error.isNotEmpty()) {
                    Log.e("collectUserData", "collectCategoryData Error: ${it.error}")
                    Toast.makeText(activity, it.error, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    viewModel.showProgressBar(false)
                    getDataBinding().userdataLayoout.userDetails = it.data
                    Log.i("GetData", "observeData: ${it.data}")
                    Log.i("collectUserData", "collectUserData: ${it.data}")
                }

            }
        }
    }

    private fun collectCartsData() {
        lifecycleScope.launch {
            viewModel.userCartsData.collect {
                if (it.isLoading) {
                    viewModel.showProgressBar(true)
                } else if (it.error.isNotEmpty()) {
                    Toast.makeText(activity, it.error, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    viewModel.showProgressBar(false)
                    val products = it.data?.carts?.firstOrNull()?.products
                    if (products != null) {
                        val adapter =
                            ProductAdapter(requireContext(), products, onItemClick = { id ->
                                Log.i("onitemclick", "observeData: $$id")

                                viewModel.productDetailsCallAPI(id)
                                productDetails()


                            })
                        getDataBinding().rcvCart.adapter = adapter
                        getDataBinding().rcvCart.visibility = View.VISIBLE
                    }
                    Log.i("Carts", "observeData: ${it.data}")
                }
            }
        }
    }

    private fun productDetails() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.productDetails.collect {
                if (it.isLoading) {
                    viewModel.showProgressBar(true)
                    Log.i("productDetails", "Loading")
                } else if (it.error.isNotEmpty()) {
                    Log.e("productDetails", "collectCategoryData Error: ${it.error}")
                    Toast.makeText(activity, it.error, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    viewModel.showProgressBar(false)
                    if (it.data != null) {

                        ProductDetailsHelper.productDetailsData = it.data
                        navigateProductDetails(it.data)
                        Log.i("2181", "is not null: ${it.data}")
                        // showDialog(it.data.description)

                    }
                }

            }
        }
    }

    private fun navigateProductDetails(productDetails: ProductDetails?) {
        if (productDetails != null) {
            findNavController().navigate(
                GetDataFragmentDirections.actionGetDataFragmentToProductDetials(
                    productDetails
                )
            )
        }
    }

}