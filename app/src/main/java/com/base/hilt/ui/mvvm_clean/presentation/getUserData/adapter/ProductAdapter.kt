package com.base.hilt.ui.mvvm_clean.presentation.getUserData.adapter

import android.content.Context
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.base.hilt.R
import com.base.hilt.bind.GenericRecyclerViewAdapter
import com.base.hilt.databinding.FeedResultLayoutBinding
import com.base.hilt.ui.mvvm_clean.data.getUserData.Carts.Cart.Product
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions


class ProductAdapter(
    context: Context, val carts: List<Product?>,
    private val onItemClick: (productId: Int) -> Unit
) : GenericRecyclerViewAdapter<Product, FeedResultLayoutBinding>(
    context,
    carts as ArrayList<Product?>
) {
    override val layoutResId: Int
        get() = R.layout.feed_result_layout

    override fun getLayoutRes(model: Product): Int = layoutResId

    override fun onItemClick(
        model: Product,
        position: Int,
        dataBinding: FeedResultLayoutBinding
    ) {
        model.id?.let { onItemClick.invoke(position + 1) }
    }

    override fun onBindData(
        model: Product,
        position: Int,
        dataBinding: FeedResultLayoutBinding
    ) {
        Log.i("MobileDataAdapter", "MobileDataAdapter: ${model.title}")
        dataBinding.product = model
    }


}

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
    Glide.with(view.context)
        .load(imageUrl)
        .placeholder(R.drawable.ic_launcher_background)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(view)
}