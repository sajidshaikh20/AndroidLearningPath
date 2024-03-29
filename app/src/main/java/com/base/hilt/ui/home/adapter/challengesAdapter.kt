package com.base.hilt.ui.home.adapter

import android.content.Context
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.base.hilt.R
import com.base.hilt.bind.GenericRecyclerViewAdapter
import com.base.hilt.databinding.ListInvitesHomeBinding
import com.base.hilt.ui.model.Challenges
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions


class challengesAdapter(context: Context, val list:ArrayList<Challenges?>)

    : GenericRecyclerViewAdapter<Challenges, ListInvitesHomeBinding>(context,list){

    override val layoutResId: Int
        get() = R.layout.list_invites_home

    override fun getLayoutRes(model: Challenges): Int {
       return layoutResId
    }

    override fun onItemClick(
        model: Challenges,
        position: Int,
        dataBinding: ListInvitesHomeBinding
    ) {

    }

    override fun onBindData(model: Challenges, position: Int, dataBinding: ListInvitesHomeBinding) {
        dataBinding.model =model
        Log.i("2181", "onBindData: ${model.author?.avatar}")
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