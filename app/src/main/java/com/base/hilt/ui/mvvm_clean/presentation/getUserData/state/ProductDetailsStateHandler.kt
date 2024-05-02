package com.base.hilt.ui.mvvm_clean.presentation.getUserData.state

import com.base.hilt.ui.mvvm_clean.data.getUserData.ProductDetails


data class ProductDetailsStateHandler(
    val isLoading: Boolean = false,
    val data: ProductDetails? = null,
    val error: String = ""
)