package com.base.hilt.bind

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.base.hilt.R
import com.base.hilt.databinding.RowItemHomeListBinding
import com.base.hilt.ui.home.Vendor
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import java.io.File

/**
 * Bind data used for data binding
 */
class BindAdapters {
    companion object {

        @BindingAdapter(value = ["bind:vendor_items_list"], requireAll = false)
        @JvmStatic
        fun bindVendorItemList(
            view: RecyclerView,
            subscription_meal_items_list: ArrayList<Vendor>?
        ) {
            if (subscription_meal_items_list != null) {
                val adapter = object :
                    GenericRecyclerViewAdapter<Vendor, RowItemHomeListBinding>(
                        view.context,
                        subscription_meal_items_list
                    ) {
                    override val layoutResId: Int
                        get() = R.layout.row_item_home_list

                    override fun onBindData(
                        model: Vendor,
                        position: Int,
                        dataBinding: RowItemHomeListBinding
                    ) {
                        dataBinding.model = model
                        dataBinding.executePendingBindings()
                    }

                    override fun onItemClick(model: Vendor, position: Int) {

                    }
                }
                view.adapter = adapter
            }
        }


        /**
         * Load Image in imageview using Glide
         * @param imageView AppCompatImageView imageview object
         * @param url String? URL
         */
        @SuppressLint("CheckResult")
        @BindingAdapter(value = ["bind:imageSet", "bind:placeHolder"], requireAll = false)
        @JvmStatic
        fun bindImageData(imageView: AppCompatImageView, url: String?, placeHolder: Drawable?) {
            if (url == null || url.isEmpty()) {
                if (placeHolder != null) {
                    imageView.setImageDrawable(placeHolder)
                } else {
                    imageView.setImageResource(R.drawable.ic_launcher_background)
                }
                return
            } else {
                var placeHolder1: Drawable =
                    ContextCompat.getDrawable(
                        imageView.context,
                        R.drawable.ic_launcher_background
                    )!!
                if (placeHolder != null)
                    placeHolder1 = placeHolder
                val requestOptions = RequestOptions()
                requestOptions.placeholder(placeHolder1)
                requestOptions.error(placeHolder1)
                requestOptions.dontAnimate()
                requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
                when {
                    url.contains("https://") || url.contains("http://") -> {
                        Glide.with(imageView.context).setDefaultRequestOptions(requestOptions)
                            .load(url)
                            .into(imageView)
                    }
                    url.contains("R.drawable") -> {
                        Glide.with(imageView.context).setDefaultRequestOptions(requestOptions).load(
                            imageView.context.resources.getIdentifier(
                                url.replace(
                                    "R.drawable.",
                                    ""
                                ), "drawable", imageView.context.packageName
                            )
                        )
                            .into(imageView)
                    }
                    else -> {
                        Glide.with(imageView.context).setDefaultRequestOptions(requestOptions)
                            .load(File(url)).into(imageView)
                    }
                }
            }
        }

    }
}
