package com.base.hilt.ui.home.adapter

import android.content.Context
import com.base.hilt.R
import com.base.hilt.bind.GenericRecyclerViewAdapter
import com.base.hilt.databinding.FeedResultLayoutBinding
import com.base.hilt.ui.home.Character
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions


class FeedResultAdapter(
    context: Context, sizeList: ArrayList<Character>
) : GenericRecyclerViewAdapter<Character, FeedResultLayoutBinding>(
    context,
    sizeList
) {
    private var selectedItemPosition: Int = -1
    override val layoutResId: Int
        get() = R.layout.feed_result_layout
    override fun onItemClick(model: Character, position: Int) {

    }

    override fun onBindData(
        model: Character,
        position: Int,
        dataBinding: FeedResultLayoutBinding
    ) {

        dataBinding.tvCardName.text = model.name
        dataBinding.tvid.text = model.id

        Glide.with(dataBinding.root)
            .load(model.image)
            .placeholder(R.drawable.ic_launcher_background)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(dataBinding.imgProfile)

        dataBinding.tvGender.text = model.gender
        dataBinding.tvSpecies.text = model.species
    }

    private fun handleItemClick(position: Int) {
        selectedItemPosition = position
        notifyDataSetChanged()
    }


}