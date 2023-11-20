package com.ssafy.smartstore_jetpack.src.main.my.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.config.ApplicationClass
import com.ssafy.smartstore_jetpack.databinding.ListItemOrderDetailListBinding
import com.ssafy.smartstore_jetpack.src.main.my.models.OrderDetailResponse
import com.ssafy.smartstore_jetpack.util.CommonUtils

private const val TAG = "OrderAdapter_싸피"
class OrderDetailListAdapter: ListAdapter<OrderDetailResponse, OrderDetailListAdapter.OrderDetailListHolder>(ODComparator){

    companion object ODComparator: DiffUtil.ItemCallback<OrderDetailResponse>(){
        override fun areItemsTheSame(oldItem: OrderDetailResponse, newItem: OrderDetailResponse): Boolean {
            return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
        }

        override fun areContentsTheSame(oldItem: OrderDetailResponse, newItem: OrderDetailResponse): Boolean {
            return oldItem == newItem
        }
    }

    inner class OrderDetailListHolder(val binding: ListItemOrderDetailListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindInfo(data: OrderDetailResponse){
            var type = if(data.productType == "coffee") "잔" else "개"

            Glide.with(itemView)
                .load("${ApplicationClass.MENU_IMGS_URL}${data.img}")
                .into(binding.menuImage)

            binding.textShoppingMenuName.text = data.productName
            binding.textShoppingMenuMoney.text = CommonUtils.makeComma(data.unitPrice)
            binding.textShoppingMenuCount.text = "${data.quantity} ${type}"
            binding.textShoppingMenuMoneyAll.text = CommonUtils.makeComma(data.unitPrice * data.quantity)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailListHolder {
        val binding = ListItemOrderDetailListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderDetailListHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderDetailListHolder, position: Int) {
        holder.bindInfo(getItem(position))
    }

}

