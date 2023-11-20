package com.ssafy.smartstore_jetpack.src.main.menu

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.config.ApplicationClass
import com.ssafy.smartstore_jetpack.config.BaseFragment
import com.ssafy.smartstore_jetpack.databinding.DialogMenuCommentBinding
import com.ssafy.smartstore_jetpack.databinding.FragmentMenuDetailBinding
import com.ssafy.smartstore_jetpack.dto.Comment
import com.ssafy.smartstore_jetpack.dto.OrderDetail
import com.ssafy.smartstore_jetpack.src.main.MainActivity
import com.ssafy.smartstore_jetpack.src.main.MainActivityViewModel
import com.ssafy.smartstore_jetpack.src.main.menu.adapter.CommentAdapter
import com.ssafy.smartstore_jetpack.src.main.menu.models.MenuDetailWithCommentResponse
import com.ssafy.smartstore_jetpack.util.CommonUtils
import com.ssafy.smartstore_jetpack.util.RetrofitUtil
import kotlinx.coroutines.launch
import kotlin.math.round

//메뉴 상세 화면 . Order탭 - 특정 메뉴 선택시 열림
private const val TAG = "MenuDetailFragment_싸피"
class MenuDetailFragment : BaseFragment<FragmentMenuDetailBinding>(FragmentMenuDetailBinding::bind, R.layout.fragment_menu_detail){
    private lateinit var mainActivity: MainActivity
    private var commentAdapter = CommentAdapter()

    private val activityViewModel:MainActivityViewModel by activityViewModels()
    private var rating = 0f
    private var isAdmin = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideBottomNav(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isAdmin = ApplicationClass.sharedPreferencesUtil.getUser().isAdmin

        binding.btnAddCount.setOnClickListener {
            var count = binding.textMenuCount.text.toString().toInt()
            binding.textMenuCount.text = (count + 1).toString()
        }

        binding.btnMinusCount.setOnClickListener {
            var count = binding.textMenuCount.text.toString().toInt()
            if (count > 0) {
                binding.textMenuCount.text = (count - 1).toString()
            }
        }

        registerObserver()
        activityViewModel.getProductInfo(activityViewModel.productId.value!!)

        initListener()

        binding.recyclerViewMenuDetail.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = commentAdapter
        }

    }

    private fun registerObserver(){
        activityViewModel.productInfo.observe(viewLifecycleOwner) {
            Log.d(TAG, "registerObserver: $it")
            // comment 가 없을 경우 -> 들어온 response가 1개이고 해당 userId 가 null일 경우 빈 배열 Adapter 연결
            commentAdapter.submitList(it)
            // 화면 정보 갱신
            setScreen(it[0])
        }
    }

    // 초기 화면 설정
    private fun setScreen(menu: MenuDetailWithCommentResponse){
        Log.d(TAG, "setScreen: $menu")
        Glide.with(this)
            .load("${ApplicationClass.MENU_IMGS_URL}${menu.productImg}")
            .into(binding.menuImage)

        if(isAdmin){
            binding.btnAddList.text = "상품 정보 수정"
            binding.orderQuantityInfo.visibility = View.GONE
            binding.addCommentLayout.visibility = View.GONE
        }
        binding.txtMenuName.text = menu.productName
        binding.txtMenuPrice.text = "${CommonUtils.makeComma(menu.productPrice)}"
        binding.txtRating.text = "${(round(menu.productRatingAvg*10) /10)}점"
        binding.ratingBar.rating = menu.productRatingAvg.toFloat()/2
    }

    private fun initListener(){
        binding.btnAddList.setOnClickListener {
            if(!isAdmin){
                val curItem = activityViewModel.productInfo.value!![0]

                val orderDetail = OrderDetail(activityViewModel.productId.value!!, binding.textMenuCount.text.toString().toInt()).also {
                    it.img = curItem.productImg
                    it.unitPrice = curItem.productPrice
                    it.productName = curItem.productName
                    it.productType = curItem.type
                }
                activityViewModel.addToShoppingList(orderDetail)
                Toast.makeText(context,"상품이 장바구니에 담겼습니다.",Toast.LENGTH_SHORT).show()
                Navigation.findNavController(requireView()).popBackStack()
            }else{
                Navigation.findNavController(requireView()).navigate(R.id.menuAddFragment)
            }

        }
        binding.btnCreateComment.setOnClickListener {
            if(binding.etComment.text.isNotEmpty())
                showDialogRatingStar()
        }

        //adapter 버튼 처리
        commentAdapter.setItemClickListener(object: CommentAdapter.ItemClickListener{
            override fun onUpdate(view: View, data: MenuDetailWithCommentResponse, newComment:String) {
                //내가 작성한 글인것처럼 업데이트 합니다. 수정해서 써야합니다.
                val comment = Comment(
                    data.commentId,
                    ApplicationClass.sharedPreferencesUtil.getUser().id,
                    activityViewModel.productId.value!!,
                    data.productRating.toFloat(),
                    newComment)
                lifecycleScope.launch {
                    val bool = RetrofitUtil.commentService.update(comment)
                    if(bool) activityViewModel.getProductInfo(activityViewModel.productId.value!!)
                }
            }

            override fun onDelete(view: View, id: Int) {
                lifecycleScope.launch{
                    val bool = RetrofitUtil.commentService.delete(id)
                    if(bool) activityViewModel.getProductInfo(activityViewModel.productId.value!!)
                }

            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivity.hideBottomNav(false)
    }

    private fun showDialogRatingStar() {
        val ratingDialog = Dialog(requireContext())
        val ratingDialogBinding = DialogMenuCommentBinding.inflate(layoutInflater)
        ratingDialog.setContentView(ratingDialogBinding.root)
        ratingDialog.show()
        ratingDialogBinding.ratingBarMenuDialogComment.setOnRatingBarChangeListener { ratingBar, fl, b ->
            rating = fl*2
        }
        ratingDialogBinding.confirm.setOnClickListener {
            addComment()
            ratingDialog.dismiss()
        }
        ratingDialogBinding.cancel.setOnClickListener {
            ratingDialog.dismiss()
        }
    }

    private fun addComment(){
        lifecycleScope.launch{
            val bool = RetrofitUtil.commentService.insert(
                Comment(
                    userId = ApplicationClass.sharedPreferencesUtil.getUser().id,
                    productId = activityViewModel.productId.value!!,
                    rating = rating,
                    comment = binding.etComment.text.toString()
                )
            )
            if(bool) activityViewModel.getProductInfo(activityViewModel.productId.value!!)
            binding.etComment.setText("")
        }
    }


}