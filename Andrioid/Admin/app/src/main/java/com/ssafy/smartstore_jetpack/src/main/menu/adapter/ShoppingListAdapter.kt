package com.ssafy.smartstore_jetpack.src.main.menu.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.smartstore_jetpack.config.ApplicationClass
import com.ssafy.smartstore_jetpack.databinding.ListItemShoppingListBinding
import com.ssafy.smartstore_jetpack.dto.OrderDetail

private const val TAG = "ShoppingListAdapter_싸피"

class ShoppingListAdapter(val isAdmin: Boolean = false): ListAdapter<OrderDetail, ShoppingListAdapter.ShoppingListHolder>(OrderDetailComparator){

    companion object OrderDetailComparator: DiffUtil.ItemCallback<OrderDetail>(){
        override fun areItemsTheSame(oldItem: OrderDetail, newItem: OrderDetail): Boolean {
            return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
        }

        override fun areContentsTheSame(oldItem: OrderDetail, newItem: OrderDetail): Boolean {
            return oldItem == newItem
        }

    }

    inner class ShoppingListHolder(private val binding: ListItemShoppingListBinding) : RecyclerView.ViewHolder(binding.root){

        fun bindInfo(data: OrderDetail){
            if(isAdmin)
                binding.cancelButton.visibility = View.GONE

            Glide.with(binding.root)
                .load("${ApplicationClass.MENU_IMGS_URL}${data.img}")
                .into(binding.menuImage)
            binding.textShoppingMenuName.text = data.productName
            binding.textShoppingMenuCount.text = data.quantity.toString()
            binding.textShoppingMenuMoney.text = data.unitPrice.toString()
            binding.textShoppingMenuMoneyAll.text = (data.unitPrice * data.quantity).toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListHolder {
        val binding =
            ListItemShoppingListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShoppingListHolder(binding).apply{
            binding.cancelButton.setOnClickListener {
                buttonClickListener.onClick(layoutPosition)
            }
        }
    }

    override fun onBindViewHolder(holder: ShoppingListHolder, position: Int) {
        holder.bindInfo(getItem(position))
    }

    interface ButtonClickListener{
        fun onClick(position: Int)
    }

    lateinit var buttonClickListener: ButtonClickListener
}