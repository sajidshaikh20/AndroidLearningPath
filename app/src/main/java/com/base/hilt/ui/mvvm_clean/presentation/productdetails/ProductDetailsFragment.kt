package com.base.hilt.ui.mvvm_clean.presentation.productdetails

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.navArgs
import com.base.hilt.R
import com.base.hilt.base.FragmentBase
import com.base.hilt.base.ToolbarModel
import com.base.hilt.databinding.FragmentProductDetailsBinding
import com.base.hilt.ui.mvvm_clean.data.getUserData.ProductDetails
import com.base.hilt.ui.mvvm_clean.data.helper.ProductDetailsHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailsFragment :
    FragmentBase<ProductDetailsViewModel, FragmentProductDetailsBinding>() {


    var productDetails: ProductDetails? = null


    override fun getLayoutId(): Int {
        return R.layout.fragment_product_details
    }

    override fun setupToolbar() {
        viewModel.setToolbarItems(
            ToolbarModel(
                true, "Product Details", isBottomNavVisible = false, isSetting = false
            )
        )
    }

    override fun initializeScreenVariables() {

        //safe args initialize
        val args: ProductDetailsFragmentArgs by navArgs()


        Log.i("safe_args", "safe args: ${args.productDetails}")
        Log.i("objectHelper", "object helper: ${ProductDetailsHelper.productDetailsData}")
        productDetails = args.productDetails

        productDetails.let {
            getDataBinding().productDetails = it
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("2181", "onviewCreated: ${ProductDetailsHelper.productDetailsData}")
    }

    override fun getViewModelClass(): Class<ProductDetailsViewModel> =
        ProductDetailsViewModel::class.java
}