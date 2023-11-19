package com.ssafy.smartstore_jetpack.src.main.menu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartstore_jetpack.config.ApplicationClass
import com.ssafy.smartstore_jetpack.databinding.ListItemCommentBinding
import com.ssafy.smartstore_jetpack.src.main.menu.models.MenuDetailWithCommentResponse


class CommentAdapter(var list:List<MenuDetailWithCommentResponse> ) :RecyclerView.Adapter<CommentAdapter.CommentHolder>(){

    inner class CommentHolder(private val binding: ListItemCommentBinding) : RecyclerView.ViewHolder(binding.root){

        fun bindInfo(data : MenuDetailWithCommentResponse){
            val user = ApplicationClass.sharedPreferencesUtil.getUser()
            val item = list[layoutPosition]
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
                itemClickListner.onDelete(it, data.commentId)
            }


            binding.ivModifyAcceptComment.setOnClickListener{
                itemClickListner.onUpdate(it, data, binding.etCommentContent.text.toString())
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
        holder.bindInfo(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    //클릭 인터페이스 정의 사용하는 곳에서 만들어준다.
    interface ItemClickListener {
        fun onUpdate(view: View, data: MenuDetailWithCommentResponse, newComment:String)
        fun onDelete(view: View, id: Int)
    }
    //클릭리스너 선언
    private lateinit var itemClickListner: ItemClickListener
    //클릭리스너 등록 매소드
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }

}

