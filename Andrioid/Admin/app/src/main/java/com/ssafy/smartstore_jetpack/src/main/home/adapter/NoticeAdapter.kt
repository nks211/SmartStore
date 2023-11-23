package com.ssafy.smartstore_jetpack.src.main.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.databinding.ListItemNoticeBinding
import com.ssafy.smartstore_jetpack.dto.Note


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
            binding.textNoticeContent.text = data.content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeHolder {
        val binding = ListItemNoticeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoticeHolder(binding)
    }
    override fun onBindViewHolder(holder: NoticeHolder, position: Int) {
        holder.bindInfo(getItem(position))
    }
}

