package com.ssafy.smartstore_jetpack.src.main.my

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.config.BaseFragment
import com.ssafy.smartstore_jetpack.databinding.FragmentOrderDetailBinding
import com.ssafy.smartstore_jetpack.src.main.MainActivity
import com.ssafy.smartstore_jetpack.src.main.MainActivityViewModel
import com.ssafy.smartstore_jetpack.src.main.my.models.OrderDetailResponse
import com.ssafy.smartstore_jetpack.util.CommonUtils
import com.ssafy.smartstore_jetpack.util.RetrofitUtil
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// 주문상세화면, My탭  - 주문내역 선택시 팝업
private const val TAG = "OrderDetailFragment_싸피"
class OrderDetailFragment : BaseFragment<FragmentOrderDetailBinding>(FragmentOrderDetailBinding::bind, R.layout.fragment_order_detail) {
    private lateinit var orderDetailListAdapter: OrderDetailListAdapter
    private lateinit var mainActivity: MainActivity

    private val activityViewModel: MainActivityViewModel by activityViewModels()

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

        activityViewModel.orderId.observe(viewLifecycleOwner){
            initData(it)
        }
    }

    private fun initData(orderId:Int) {
        lifecycleScope.launch{
            RetrofitUtil.orderService.getOrderDetail(orderId).let{
                orderDetailListAdapter = OrderDetailListAdapter(mainActivity, it)

                binding.recyclerViewOrderDetailList.apply {
                    val linearLayoutManager = LinearLayoutManager(context)
                    linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                    layoutManager = linearLayoutManager
                    adapter = orderDetailListAdapter
                }

                setOrderDetailScreen(it)

                Log.d(TAG, "onViewCreated: $it")
            }

        }
    }

    // OrderDetail 페이지 화면 구성
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun setOrderDetailScreen(orderDetails: List<OrderDetailResponse>) {
        val dateFormat = SimpleDateFormat("yyyy.MM.dd HH시 mm분 ss초")
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        binding.tvOrderStatus.text = CommonUtils.isOrderCompleted(orderDetails[0])
        binding.tvOrderDate.text = dateFormat.format(orderDetails[0].orderDate)
        var totalPrice = 0
        orderDetails.forEach { totalPrice += it.totalPrice }
        binding.tvTotalPrice.text = "$totalPrice 원"
    }


    override fun onDestroy() {
        super.onDestroy()
        mainActivity.hideBottomNav(false)
    }
}