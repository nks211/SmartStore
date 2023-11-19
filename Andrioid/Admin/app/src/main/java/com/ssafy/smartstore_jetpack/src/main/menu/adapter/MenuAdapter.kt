package com.ssafy.smartstore_jetpack.src.main.menu.adapter

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
import com.ssafy.smartstore_jetpack.databinding.ListItemMenuBinding
import com.ssafy.smartstore_jetpack.dto.Product

class MenuAdapter : ListAdapter<Product, MenuAdapter.MenuHolder>(MenuComparator){

    companion object MenuComparator: DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    inner class MenuHolder(val binding: ListItemMenuBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindInfo(product : Product){
            binding.textMenuNames.text = product.name
            Glide.with(itemView)
                .load("${ApplicationClass.MENU_IMGS_URL}${product.img}")
                .into(binding.menuImage)

            itemView.setOnClickListener{
                itemClickListner.onClick(it, layoutPosition, getItem(layoutPosition).id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuHolder {
        val binding = ListItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuHolder, position: Int) {
        holder.apply{
            bindInfo(getItem(position))
        }
    }

    //클릭 인터페이스 정의 사용하는 곳에서 만들어준다.
    interface ItemClickListener {
        fun onClick(view: View, position: Int, productId:Int)
    }
    //클릭리스너 선언
    private lateinit var itemClickListner: ItemClickListener
    //클릭리스너 등록 매소드
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }
}