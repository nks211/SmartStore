package com.ssafy.smartstore_jetpack.src.main.menu.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartstore_jetpack.config.ApplicationClass
import com.ssafy.smartstore_jetpack.databinding.ListItemCommentBinding
import com.ssafy.smartstore_jetpack.dto.ReComment
import com.ssafy.smartstore_jetpack.src.main.menu.models.MenuDetailWithCommentResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CommentAdapter : ListAdapter<MenuDetailWithCommentResponse, CommentAdapter.CommentHolder>(CommentComparator){

    companion object CommentComparator:DiffUtil.ItemCallback<MenuDetailWithCommentResponse>(){
        override fun areItemsTheSame(
            oldItem: MenuDetailWithCommentResponse, newItem: MenuDetailWithCommentResponse
        ): Boolean {
            return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
        }

        override fun areContentsTheSame(oldItem: MenuDetailWithCommentResponse, newItem: MenuDetailWithCommentResponse
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class CommentHolder(private val binding: ListItemCommentBinding) : RecyclerView.ViewHolder(binding.root){

        fun bindInfo(data : MenuDetailWithCommentResponse){
            val user = ApplicationClass.sharedPreferencesUtil.getUser()
            val item = getItem(layoutPosition)
            binding.textNoticeContent.text = data.commentContent

            binding.etCommentContent.visibility = View.GONE
            binding.ivModifyAcceptComment.visibility = View.GONE
            binding.ivModifyCancelComment.visibility = View.GONE

            if(user.id != item.userId){
                binding.ivModifyComment.visibility = View.GONE
                binding.ivDeleteComment.visibility = View.GONE
            }else{
                binding.ivModifyComment.visibility = View.VISIBLE
                binding.ivDeleteComment.visibility = View.VISIBLE
            }

            binding.ivReplyOpen.setOnClickListener {
                binding.reComment.visibility = View.VISIBLE
                binding.ivReplyClose.visibility = View.VISIBLE
                binding.ivReplyOpen.visibility = View.GONE
                binding.tvReCommentContent.visibility = View.VISIBLE
                CoroutineScope(Dispatchers.Main).launch {
                    var reComment = withContext(Dispatchers.IO){
                        itemClickListener.onOpenReply(data.commentId)
                    }
                    if(reComment.comment.isEmpty()){
                        binding.tvReCommentContent.text = "답글을 입력해 주세요"
                        binding.tvReCommentContent.setTextColor(Color.GRAY)
                        binding.ivDeleteReComment.visibility = View.GONE
                        binding.ivModifyAcceptReComment.setOnClickListener {
                            itemClickListener.onSaveReply(ReComment(data.commentId, binding.etReCommentContent.text.toString()))
                            binding.ivModifyAcceptReComment.visibility = View.GONE
                            binding.ivModifyCancelReComment.visibility = View.GONE
                            binding.tvReCommentContent.visibility = View.VISIBLE
                            binding.tvReCommentContent.text = binding.etReCommentContent.text.toString()
                            binding.etReCommentContent.visibility = View.GONE
                        }
                    }else{
                        binding.tvReCommentContent.text = reComment.comment
                        binding.ivDeleteReComment.setOnClickListener {
                            itemClickListener.onDelete(it, reComment.id)
                        }
                        binding.ivModifyAcceptReComment.setOnClickListener {
                            itemClickListener.onUpdateReply(ReComment(reComment.id, reComment.commentId, binding.etReCommentContent.text.toString()))
                        }
                    }
                }
            }

            binding.ivReplyClose.setOnClickListener {
                binding.reComment.visibility = View.GONE
                binding.ivReplyClose.visibility = View.GONE
                binding.ivReplyOpen.visibility = View.VISIBLE
            }

            binding.ivModifyReComment.setOnClickListener {
                binding.ivModifyAcceptReComment.visibility = View.VISIBLE
                binding.ivModifyCancelReComment.visibility = View.VISIBLE
                binding.ivModifyReComment.visibility = View.GONE
                binding.ivDeleteReComment.visibility = View.GONE

                binding.etReCommentContent.visibility = View.VISIBLE
                binding.etReCommentContent.setText(binding.tvReCommentContent.text)
                binding.tvReCommentContent.visibility = View.GONE
            }

            binding.ivModifyComment.setOnClickListener{
                binding.ivModifyAcceptComment.visibility = View.VISIBLE
                binding.ivModifyCancelComment.visibility = View.VISIBLE
                binding.ivModifyComment.visibility = View.GONE
                binding.ivDeleteComment.visibility = View.GONE

                binding.etCommentContent.visibility = View.VISIBLE
                binding.etCommentContent.setText(binding.textNoticeContent.text)
                binding.textNoticeContent.visibility = View.GONE
            }

            binding.ivDeleteComment.setOnClickListener{
                itemClickListener.onDelete(it, data.commentId)
            }



            binding.ivModifyAcceptComment.setOnClickListener{
                itemClickListener.onUpdate(it, data, binding.etCommentContent.text.toString())
                binding.ivModifyAcceptComment.visibility = View.GONE
                binding.ivModifyCancelComment.visibility = View.GONE
                binding.etCommentContent.visibility = View.GONE
                binding.textNoticeContent.visibility = View.VISIBLE
            }

            binding.ivModifyCancelComment.setOnClickListener {
                binding.ivModifyAcceptComment.visibility = View.GONE
                binding.ivModifyCancelComment.visibility = View.GONE
                binding.ivModifyComment.visibility = View.VISIBLE
                binding.ivDeleteComment.visibility = View.VISIBLE

                binding.etCommentContent.visibility = View.GONE
                binding.textNoticeContent.visibility = View.VISIBLE
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder {
        val binding = ListItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        holder.bindInfo(getItem(position))
    }

    //클릭 인터페이스 정의 사용하는 곳에서 만들어준다.
    interface ItemClickListener {
        fun onUpdate(view: View, data: MenuDetailWithCommentResponse, newComment:String)
        fun onDelete(view: View, id: Int)
        suspend fun onOpenReply(id: Int): ReComment
        fun onSaveReply(reComment: ReComment)
        fun onUpdateReply(reComment: ReComment)
        fun onDeleteReply(id: Int)
    }
    //클릭리스너 선언
    private lateinit var itemClickListener: ItemClickListener
    //클릭리스너 등록 매소드
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

}

