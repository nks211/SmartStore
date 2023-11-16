package com.ssafy.smartstore_jetpack.src.main.menu

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.config.ApplicationClass
import com.ssafy.smartstore_jetpack.databinding.ListItemShoppingListBinding
import com.ssafy.smartstore_jetpack.dto.OrderDetail
import com.ssafy.smartstore_jetpack.src.main.menu.models.MenuDetailWithCommentResponse
import com.ssafy.smartstore_jetpack.src.main.my.models.OrderDetailResponse

private const val TAG = "ShoppingListAdapter_싸피"
class ShoppingListAdapter(var list: ArrayList<OrderDetail>) :RecyclerView.Adapter<ShoppingListAdapter.ShoppingListHolder>(){

    inner class ShoppingListHolder(private val binding: ListItemShoppingListBinding) : RecyclerView.ViewHolder(binding.root){

        fun bindInfo(data: OrderDetail){
            Log.d(TAG, "bindInfo: $data")
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
        val binding = ListItemShoppingListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShoppingListHolder(binding).apply{
            binding.cancelButton.setOnClickListener {
                buttonClickListener.onClick(layoutPosition)
            }
        }
    }

    override fun onBindViewHolder(holder: ShoppingListHolder, position: Int) {
        holder.bindInfo(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ButtonClickListener{
        fun onClick(position: Int)
    }

    lateinit var buttonClickListener: ButtonClickListener
}

