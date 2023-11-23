package com.ssafy.smartstore_jetpack.src.main.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.databinding.ListItemNoticeBinding
import com.ssafy.smartstore_jetpack.dto.Note
import com.ssafy.smartstore_jetpack.src.main.MainActivityViewModel
import com.ssafy.smartstore_jetpack.util.RetrofitUtil


class NoticeAdapter:ListAdapter<Note, NoticeAdapter.NoticeHolder>(NoteComparator){

    companion object NoteComparator: DiffUtil.ItemCallback<Note>(){
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }

    inner class NoticeHolder(val binding: ListItemNoticeBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindInfo(data: Note){
            if(data.title=="orderNote" || data.title == "ordernote"){
                binding.textNoticeContent.text = "${data.content}번 주문이 들어왔습니다."
            }
            binding.delete.setOnClickListener {
                itemClickListener.onDeleteClick(data.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeHolder {
        val binding = ListItemNoticeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoticeHolder(binding)
    }
    override fun onBindViewHolder(holder: NoticeHolder, position: Int) {
        holder.bindInfo(getItem(position))
    }

    interface ItemClickListener{
        fun onDeleteClick(id: Int)
    }

    private lateinit var itemClickListener: ItemClickListener

    fun setItemClickListener(listener: ItemClickListener){
        itemClickListener = listener
    }
}

