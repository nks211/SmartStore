package com.ssafy.smartstore_jetpack.src.main.my

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ssafy.smartstore_jetpack.BuildConfig
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.config.ApplicationClass
import com.ssafy.smartstore_jetpack.config.BaseFragment
import com.ssafy.smartstore_jetpack.databinding.FragmentMypageBinding
import com.ssafy.smartstore_jetpack.dto.Grade
import com.ssafy.smartstore_jetpack.src.main.MainActivity
import com.ssafy.smartstore_jetpack.src.main.MainActivityViewModel
import com.ssafy.smartstore_jetpack.src.main.my.adapter.CompletedListAdapter
import com.ssafy.smartstore_jetpack.src.main.my.adapter.OrderListAdapter
import com.ssafy.smartstore_jetpack.util.CommonUtils
import com.ssafy.smartstore_jetpack.util.RetrofitUtil
import kotlinx.coroutines.launch

// MyPage 탭
private const val TAG = "MypageFragment_싸피"
class MyPageFragment : BaseFragment<FragmentMypageBinding>(FragmentMypageBinding::bind, R.layout.fragment_mypage) {
    private lateinit var orderAdapter : OrderListAdapter
    private lateinit var completedOrderAdapter: CompletedListAdapter
    private lateinit var mainActivity: MainActivity
    private var isAdmin = false

    private val activityViewModel : MainActivityViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = getUserData().also {
            initData(it)
        }
        if(!isAdmin) {
            binding.levelLayout.visibility = View.VISIBLE
            binding.textLevelRest.visibility = View.VISIBLE
            getGradeData(id)
        }
    }

    private fun getUserData():String{
        val user = ApplicationClass.sharedPreferencesUtil.getUser()
        binding.textUserName.text = user.name + " 님"
        isAdmin = user.isAdmin
        Log.d(TAG, "getUserData: ${user.isAdmin}")

        return user.id
    }

    private fun getGradeData(id: String) {
        lifecycleScope.launch { 
            var userinfo = RetrofitUtil.userService.getInfowithstamp(id)
            Log.d(TAG, "getGradeData: $userinfo")
            var gradeinfo = Gson().fromJson<Grade>(userinfo["grade"].toString(), object: TypeToken<Grade>(){}.type)
            binding.imageLevel.setImageURI(Uri.parse(
                "android.resource://" + BuildConfig.APPLICATION_ID
                        + "/drawable/" + gradeinfo.img.substring(0, gradeinfo.img.indexOf("."))))
            binding.textUserLevel.text = gradeinfo.title + " " + gradeinfo.step.toString() + "단계"
            binding.textLevelRest.text = "다음 레벨까지 " + gradeinfo.to.toString() + "잔 남았습니다."
            var state = ""
            when(gradeinfo.title) {
                "씨앗" -> { state = "${10 - gradeinfo.to}/10" }
                "꽃" -> { state = "${15 - gradeinfo.to}/15" }
                "열매" -> { state = "${20 - gradeinfo.to}/20" }
                "커피콩" -> { state = "${25 - gradeinfo.to}/25" }
                "나무" -> {  }
            }
            binding.textUserNextLevel.text = state
        }
    }

    private fun initData(id:String){
        // 최근 한달간 주문내역

        if(!isAdmin){
            lifecycleScope.launch {
                val userLastOrderData = RetrofitUtil.orderService.getLastMonthOrder(id)

                orderAdapter = OrderListAdapter(mainActivity, CommonUtils.makeLatestOrderList(userLastOrderData))
                orderAdapter.setItemClickListener(object : OrderListAdapter.ItemClickListener {
                    override fun onClick(view: View, position: Int, orderid: Int) {
                        activityViewModel.setOrderId(orderid)
                        mainActivity.openFragment(2)
                    }
                })

                binding.recyclerViewOrder.apply {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapter = orderAdapter
                }
                Log.d(TAG, "onViewCreated: $userLastOrderData")
            }
        }else{
            lifecycleScope.launch{
                val completedOrderData = RetrofitUtil.orderService.getAllOrdersByResults("Y")
                completedOrderAdapter = CompletedListAdapter()
                completedOrderAdapter.setItemClickListener(object : CompletedListAdapter.ItemClickListener{
                    override fun onClick(view: View, position: Int, orderid: Int) {
                        activityViewModel.setOrderId(orderid)
                        mainActivity.openFragment(2)
                    }

                })
                completedOrderAdapter.submitList(CommonUtils.makeLatestOrderList(completedOrderData))
                binding.recyclerViewOrder.apply {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    adapter = completedOrderAdapter
                }
            }
        }
        binding.logout.setOnClickListener {
            mainActivity.openFragment(5)
        }
    }

}