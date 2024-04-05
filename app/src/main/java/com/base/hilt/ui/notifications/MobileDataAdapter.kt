package com.base.hilt.ui.notifications

import android.content.Context
import android.util.Log
import com.base.hilt.R
import com.base.hilt.bind.GenericRecyclerViewAdapter
import com.base.hilt.databinding.LayoutMobileDataBinding
import com.base.hilt.ui.notifications.model.MobileData.MobileDataItem


class MobileDataAdapter(
    mContext: Context, val data: ArrayList<MobileDataItem?>

) : GenericRecyclerViewAdapter<MobileDataItem, LayoutMobileDataBinding>(mContext, data) {

    override val layoutResId: Int = R.layout.layout_mobile_data

    override fun getLayoutRes(model: MobileDataItem): Int  = layoutResId

    override fun onBindData(
        model: MobileDataItem,
        position: Int,
        dataBinding: LayoutMobileDataBinding
    ) {

        if (model.data!=null){
            Log.i("2181", "MobileDataAdapter: ${model.id}")
            Log.i("2181", "MobileDataAdapter: ${model.name}")
            dataBinding.viewModel = model
        }
    }

    override fun onItemClick(
        model: MobileDataItem,
        position: Int,
        dataBinding: LayoutMobileDataBinding
    ) {

    }


}