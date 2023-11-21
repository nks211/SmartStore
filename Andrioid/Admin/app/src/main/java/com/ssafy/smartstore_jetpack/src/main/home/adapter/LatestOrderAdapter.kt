package com.ssafy.smartstore_jetpack.src.main.home.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.smartstore_jetpack.config.ApplicationClass
import com.ssafy.smartstore_jetpack.databinding.ListItemLatestOrderBinding
import com.ssafy.smartstore_jetpack.src.main.my.models.LatestOrderResponse
import com.ssafy.smartstore_jetpack.util.CommonUtils

private const val TAG = "LatestOrderAdapter_싸피"

class LatestOrderAdapter: ListAdapter<LatestOrderResponse, LatestOrderAdapter.LatestOrderHolder>(LatestOrderComparator){

    companion object LatestOrderComparator: DiffUtil.ItemCallback<LatestOrderResponse>(){
        override fun areItemsTheSame(
            oldItem: LatestOrderResponse,
            newItem: LatestOrderResponse
        ): Boolean {
            return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
        }

        override fun areContentsTheSame(
            oldItem: LatestOrderResponse,
            newItem: LatestOrderResponse
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class LatestOrderHolder(val binding: ListItemLatestOrderBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindInfo(data: LatestOrderResponse){
            Glide.with(itemView)
                .load("${ApplicationClass.MENU_IMGS_URL}${data.img}")
                .into(binding.menuImage)

            if(data.orderCnt > 1){
                binding.textMenuNames.text = "${data.productName} 외 ${data.orderCnt -1}건"  //외 x건
            }else{
                binding.textMenuNames.text = data.productName
            }

            binding.textMenuPrice.text = CommonUtils.makeComma(data.totalPrice)
            binding.textMenuDate.text = CommonUtils.getFormattedString(data.orderDate)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LatestOrderHolder {
        val binding = ListItemLatestOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LatestOrderHolder(binding)
    }

    override fun onBindViewHolder(holder: LatestOrderHolder, position: Int) {
//        holder.bind()
        holder.apply {
            bindInfo(getItem(position))
            //클릭연결
            itemView.setOnClickListener{
                itemClickListner.onClick(it, position)
            }
        }
    }

    //클릭 인터페이스 정의 사용하는 곳에서 만들어준다.
    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }
    //클릭리스너 선언
    private lateinit var itemClickListner: ItemClickListener
    //클릭리스너 등록 매소드
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }

}