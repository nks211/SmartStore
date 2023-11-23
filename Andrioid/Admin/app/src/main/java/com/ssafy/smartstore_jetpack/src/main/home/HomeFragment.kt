package com.ssafy.smartstore_jetpack.src.main.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.config.ApplicationClass
import com.ssafy.smartstore_jetpack.config.BaseFragment
import com.ssafy.smartstore_jetpack.databinding.FragmentHomeBinding
import com.ssafy.smartstore_jetpack.dto.Note
import com.ssafy.smartstore_jetpack.src.main.MainActivity
import com.ssafy.smartstore_jetpack.src.main.MainActivityViewModel
import com.ssafy.smartstore_jetpack.src.main.home.adapter.LatestOrderAdapter
import com.ssafy.smartstore_jetpack.src.main.home.adapter.NoticeAdapter
import com.ssafy.smartstore_jetpack.src.main.my.models.LatestOrderResponse
import com.ssafy.smartstore_jetpack.util.CommonUtils
import com.ssafy.smartstore_jetpack.util.FirebaseMessagingService
import com.ssafy.smartstore_jetpack.util.RetrofitUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Home 탭
private const val TAG = "HomeFragment_싸피"
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::bind, R.layout.fragment_home){
    private lateinit var latestOrderAdapter : LatestOrderAdapter
    private lateinit var noticeAdapter: NoticeAdapter

    private val viewModel:HomeFragmentViewModel by viewModels()
    private val mainActivityViewModel:MainActivityViewModel by activityViewModels()

    private var isAdmin = false


    private lateinit var id:String

    private lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUserData()

        if(!isAdmin){
            viewModel.getListOrderData(id)
        }else{
            mainActivityViewModel.getNewOrder()
            mainActivityViewModel.setCompletedOrder()
            mainActivityViewModel.getNotes(id)
        }

        registerObserver()
        initAdapter()
    }

    private fun initUserData(){
        val user = ApplicationClass.sharedPreferencesUtil.getUser()
        id = user.id
        binding.textUserName.text = "${user.name} 님"
        isAdmin = user.isAdmin
    }


    private fun initAdapter() {
        noticeAdapter = NoticeAdapter().apply {
            submitList(listOf())
            setItemClickListener(object : NoticeAdapter.ItemClickListener{
                override fun onDeleteClick(nId: Int) {
                    lifecycleScope.launch{
                        val bool = RetrofitUtil.noteService.delete(nId)
                        if(bool) mainActivityViewModel.getNotes(id)
                    }
                }
            })
        }
        binding.recyclerViewNoticeOrder.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = noticeAdapter
        }

        latestOrderAdapter = LatestOrderAdapter().apply {
            submitList(CommonUtils.makeLatestOrderList(listOf()))
            setItemClickListener(object : LatestOrderAdapter.ItemClickListener{
                override fun onClick(view: View, position: Int) {
                    val action = HomeFragmentDirections.actionHomeFragmentToOrderedListFragment(mainActivityViewModel.waitingOrders.value!![position].orderId)
                    Navigation.findNavController(view).navigate(action)
                }
            })
        }

        binding.recyclerViewLatestOrder.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = latestOrderAdapter
        }
    }


    // 최근 한달간 주문내역
    private fun registerObserver(){
        FirebaseMessagingService.messageReceivedListener = object : FirebaseMessagingService.OnMessageReceivedListener{
            override fun onMessageReceived() {
                if(!isAdmin)
                    viewModel.getListOrderData(id)
                else{
                    mainActivityViewModel.getNewOrder()
                    mainActivityViewModel.getNotes(id)
                }
            }
        }

        if(!isAdmin){
            viewModel.userLastOrderData.observe(viewLifecycleOwner){
                binding.orderCnt.text = "${it.size}건"
                latestOrderAdapter.submitList(it)
            }
        }else{
            mainActivityViewModel.waitingOrders.observe(viewLifecycleOwner){
                binding.orderCnt.text = "${it.size}건"
                latestOrderAdapter.submitList(it)
            }
            mainActivityViewModel.notes.observe(viewLifecycleOwner){
                noticeAdapter.submitList(it.sortedWith{  o1, o2 -> o2.orderTime.compareTo(o1.orderTime) })
            }
        }

    }
}