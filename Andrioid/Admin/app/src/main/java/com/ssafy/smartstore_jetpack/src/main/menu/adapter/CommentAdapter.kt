package com.ssafy.smartstore_jetpack.src.main.menu.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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


private const val TAG = "CommentAdapter_싸피"
class CommentAdapter : ListAdapter<MenuDetailWithCommentResponse, CommentAdapter.CommentHolder>(CommentComparator){

    companion object CommentComparator:DiffUtil.ItemCallback<MenuDetailWithCommentResponse>(){
        override fun areItemsTheSame(oldItem: MenuDetailWithCommentResponse, newItem: MenuDetailWithCommentResponse): Boolean {
            return System.identityHashCode(oldItem) == System.identityHashCode(newItem)
        }

        override fun areContentsTheSame(oldItem: MenuDetailWithCommentResponse, newItem: MenuDetailWithCommentResponse): Boolean {
            return oldItem == newItem
        }
    }

    inner class CommentHolder(private val binding: ListItemCommentBinding) : RecyclerView.ViewHolder(binding.root){

        var reComment: ReComment? = null

        fun bindInfo(data : MenuDetailWithCommentResponse){
            val user = ApplicationClass.sharedPreferencesUtil.getUser()
            val item = getItem(layoutPosition)
            binding.textNoticeContent.text = data.commentContent

            binding.etCommentContent.visibility = View.GONE
            binding.ivModifyAcceptComment.visibility = View.GONE
            binding.ivModifyCancelComment.visibility = View.GONE

            binding.ivModifyComment.visibility = if(user.id!=item.userId) View.GONE else View.VISIBLE
            binding.ivDeleteComment.visibility = if(user.id!=item.userId) View.GONE else View.VISIBLE

            binding.ivReplyOpen.setOnClickListener {
                reComment = itemClickListener.onOpenReply(data.commentId)
                onOpenReComment(true)
                if(reComment==null){
                    onReCommentWriting(true)
                }else{
                    onReCommentWriting(false)
                    binding.tvReCommentContent.text = reComment!!.comment
                    binding.ivDeleteReComment.visibility = View.VISIBLE
                }
            }

            binding.ivReplyClose.setOnClickListener {
                onOpenReComment(false)
            }

            binding.ivModifyComment.setOnClickListener{
                binding.etCommentContent.setText(binding.textNoticeContent.text)
                onCommentWriting(true)
            }

            binding.ivModifyAcceptComment.setOnClickListener{
                itemClickListener.onUpdate(it, data, binding.etCommentContent.text.toString())
                onCommentWriting(false)
            }

            binding.ivModifyCancelComment.setOnClickListener {
                onCommentWriting(false)
            }

            binding.ivDeleteComment.setOnClickListener{
                itemClickListener.onDelete(it, data.commentId)
            }

            binding.ivModifyReComment.setOnClickListener {
                binding.etReCommentContent.setText(binding.tvReCommentContent.text)
                onReCommentWriting(true)
            }

            binding.ivModifyAcceptReComment.setOnClickListener {
                reComment = itemClickListener.onOpenReply(data.commentId)

                if(binding.etReCommentContent.text.toString().isEmpty()){
                    Toast.makeText(it.context, "내용을 입력해 주세요", Toast.LENGTH_SHORT).show()
                }else{
                    if(reComment == null){
                        itemClickListener.onSaveReply(ReComment(data.commentId, binding.etReCommentContent.text.toString()))
                    }else{
                        itemClickListener.onUpdateReply(ReComment(reComment!!.id, reComment!!.commentId, binding.etReCommentContent.text.toString()))
                    }
                    binding.tvReCommentContent.text = binding.etReCommentContent.text.toString()
                    afterReCommentSave()
                }
            }

            binding.ivModifyCancelReComment.setOnClickListener {
                reComment = itemClickListener.onOpenReply(data.commentId)
                if(reComment==null){
                    onReCommentWriting(true)
                    binding.etReCommentContent.text.clear()
                }else{
                    onReCommentWriting(false)
                }
            }

            binding.ivDeleteReComment.setOnClickListener {
                reComment = itemClickListener.onOpenReply(data.commentId)
                itemClickListener.onDeleteReply(reComment!!.id)
                onReCommentWriting(true)
                binding.etReCommentContent.text.clear()
                binding.ivDeleteReComment.visibility = View.GONE
            }
        }

        private fun onOpenReComment(isOpened : Boolean){
            binding.reComment.visibility = if(isOpened) View.VISIBLE else View.GONE
            binding.ivReplyClose.visibility = if(isOpened) View.VISIBLE else View.GONE
            binding.ivReplyOpen.visibility = if(isOpened) View.GONE else View.VISIBLE
        }

        private fun onCommentWriting(isWriting: Boolean){
            binding.ivModifyAcceptComment.visibility = if(isWriting) View.VISIBLE else View.GONE
            binding.ivModifyCancelComment.visibility = if(isWriting) View.VISIBLE else View.GONE
            binding.textNoticeContent.visibility = if(isWriting) View.GONE else View.VISIBLE
            binding.etCommentContent.visibility = if(isWriting) View.VISIBLE else View.GONE
            binding.ivModifyComment.visibility = if(isWriting) View.GONE else View.VISIBLE
            binding.ivDeleteComment.visibility = if(isWriting) View.GONE else View.VISIBLE
        }

        private fun afterReCommentSave(){
            binding.tvReCommentContent.text = binding.etReCommentContent.text.toString()
            binding.tvReCommentContent.setTextColor(Color.BLACK)
            onReCommentWriting(false)
        }
        private fun onReCommentWriting(isWriting: Boolean){
            binding.ivModifyAcceptReComment.visibility = if(isWriting) View.VISIBLE else View.GONE
            binding.ivModifyCancelReComment.visibility = if(isWriting) View.VISIBLE else View.GONE
            binding.tvReCommentContent.visibility = if(isWriting) View.GONE else View.VISIBLE
            binding.etReCommentContent.visibility = if(isWriting) View.VISIBLE else View.GONE
            binding.ivModifyReComment.visibility = if(isWriting) View.GONE else View.VISIBLE
            binding.ivDeleteReComment.visibility = if(isWriting) View.GONE else View.VISIBLE
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
        fun onOpenReply(id: Int): ReComment?
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

