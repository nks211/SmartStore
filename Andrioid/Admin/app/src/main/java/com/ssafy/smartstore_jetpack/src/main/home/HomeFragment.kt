package com.ssafy.smartstore_jetpack.src.main.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.config.ApplicationClass
import com.ssafy.smartstore_jetpack.config.BaseFragment
import com.ssafy.smartstore_jetpack.databinding.FragmentHomeBinding
import com.ssafy.smartstore_jetpack.src.main.MainActivity
import com.ssafy.smartstore_jetpack.util.CommonUtils

// Home 탭
private const val TAG = "HomeFragment_싸피"
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::bind, R.layout.fragment_home){
    private lateinit var latestOrderAdapter : LatestOrderAdapter
    private var noticeAdapter: NoticeAdapter = NoticeAdapter()

    private val viewModel:HomeFragmentViewModel by viewModels()
    private lateinit var id:String

    private lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUserData()
        registerObserver()

        viewModel.getListOrderData(id)
        initAdapter()
    }

    private fun initUserData(){
        val user = ApplicationClass.sharedPreferencesUtil.getUser()
        id = user.id
        Log.d(TAG, "initUserData: $user")
        binding.textUserName.text = "${user.name} 님"
    }


    fun initAdapter() {
        noticeAdapter = NoticeAdapter()
        binding.recyclerViewNoticeOrder.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = noticeAdapter
        }
    }

    // 최근 한달간 주문내역
    private fun registerObserver(){
        viewModel.userLastOrderData.observe(viewLifecycleOwner){
            latestOrderAdapter = LatestOrderAdapter(mainActivity, CommonUtils.makeLatestOrderList(it))
            latestOrderAdapter.setItemClickListener(object : LatestOrderAdapter.ItemClickListener{
                override fun onClick(view: View, position: Int) {
                    Log.d(TAG, "onClick: ${latestOrderAdapter.list[position]}")
                    mainActivity.openFragment(1, "", latestOrderAdapter.list[position].orderId)
                }
            })
            binding.recyclerViewLatestOrder.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = latestOrderAdapter
            }
        }
    }
}