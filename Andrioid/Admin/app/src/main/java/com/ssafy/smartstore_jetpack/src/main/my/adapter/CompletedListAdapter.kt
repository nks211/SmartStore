package com.ssafy.smartstore_jetpack.src.main.my.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ssafy.smartstore_jetpack.databinding.ListItemCompletedOrderBinding
import com.ssafy.smartstore_jetpack.src.main.my.models.LatestOrderResponse
import com.ssafy.smartstore_jetpack.util.CommonUtils

private const val TAG = "CompletedListAdapter_싸피"

class CompletedListAdapter: ListAdapter<LatestOrderResponse, CompletedListAdapter.OrderHolder>(OrderComparator){

    companion object OrderComparator: DiffUtil.ItemCallback<LatestOrderResponse>(){
        override fun areItemsTheSame(oldItem: LatestOrderResponse, newItem: LatestOrderResponse): Boolean {
            return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
        }

        override fun areContentsTheSame(oldItem: LatestOrderResponse, newItem: LatestOrderResponse): Boolean {
            return oldItem == newItem
        }
    }

    inner class OrderHolder(val binding: ListItemCompletedOrderBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindInfo(data: LatestOrderResponse){
            Log.d(TAG, "bindInfo: $data")

            if(data.orderCnt > 1){
                binding.textMenuNames.text = "${data.productName} 외 ${data.orderCnt -1}건"  //외 x건
            }else{
                binding.textMenuNames.text = data.productName
            }

            binding.textMenuPrice.text = CommonUtils.makeComma(data.totalPrice)
            binding.textMenuDate.text = CommonUtils.getFormattedString(data.orderDate)
            binding.textCompleted.text = CommonUtils.isOrderCompleted(data)
            //클릭연결
            itemView.setOnClickListener{
                Log.d(TAG, "bindInfo: ${data.orderId}")
                itemClickListner.onClick(it, layoutPosition, data.orderId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        val binding = ListItemCompletedOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        holder.bindInfo(getItem(position))
    }

    //클릭 인터페이스 정의 사용하는 곳에서 만들어준다.
    interface ItemClickListener {
        fun onClick(view: View, position: Int, orderid:Int)
    }
    //클릭리스너 선언
    private lateinit var itemClickListner: ItemClickListener
    //클릭리스너 등록 매소드
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }
}