package com.ssafy.smartstore_jetpack.src.main.menu

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.config.ApplicationClass
import com.ssafy.smartstore_jetpack.config.BaseFragment
import com.ssafy.smartstore_jetpack.databinding.FragmentShoppingListBinding
import com.ssafy.smartstore_jetpack.dto.Order
import com.ssafy.smartstore_jetpack.dto.OrderDetail
import com.ssafy.smartstore_jetpack.src.main.MainActivity
import com.ssafy.smartstore_jetpack.src.main.MainActivityViewModel
import com.ssafy.smartstore_jetpack.src.main.menu.adapter.ShoppingListAdapter

private const val TAG = "OrderedListFragment"

class OrderedListFragment : BaseFragment<FragmentShoppingListBinding>(
    FragmentShoppingListBinding::bind,
    R.layout.fragment_shopping_list
){
    private val args : OrderedListFragmentArgs by navArgs()
    var orderId = -1

    private var shoppingListAdapter : ShoppingListAdapter = ShoppingListAdapter(true)
    private lateinit var mainActivity: MainActivity

    private val viewModel: MainActivityViewModel by activityViewModels()

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

        orderId = args.oId

        binding.pageTitle.text = "주문목록"
        binding.btnShop.visibility = View.GONE
        binding.btnTakeout.visibility = View.GONE
        binding.btnOrder.text = "처리 완료"

        if(orderId != -1){
            viewModel.setOrderId(orderId)
            viewModel.getOrder(orderId)
        }

        registerObserver()

        binding.recyclerViewShoppingList.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            layoutManager = linearLayoutManager
            adapter = shoppingListAdapter
            //원래의 목록위치로 돌아오게함
            adapter!!.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }

        binding.btnOrder.setOnClickListener {
            completedOrder()
        }
    }

    private fun totalAmount(): Int{
        var totalAmount = 0
        for(detail: OrderDetail in viewModel.shoppingList.value!!){
            totalAmount += detail.unitPrice * detail.quantity
        }
        return totalAmount
    }


    private fun registerObserver(){
        viewModel.shoppingList.observe(viewLifecycleOwner){
            shoppingListAdapter.submitList(it)
            binding.textShoppingCount.text = "총 ${shoppingListAdapter.itemCount}개"
            binding.textShoppingMoney.text = "${totalAmount()} 원"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivity.hideBottomNav(false)
    }

    private fun completedOrder(){

        viewModel.completeOrder(
            Order().apply {
                id = orderId
                userId = ApplicationClass.sharedPreferencesUtil.getUser().id
                details.addAll(ArrayList(viewModel.shoppingList.value!!))
                completed = "Y"
                topImg = viewModel.shoppingList.value!![0].img
                totalQnanty = shoppingListAdapter.itemCount
                totalPrice = totalAmount()
                topProductName = viewModel.shoppingList.value!![0].productName
            }
        )
        Toast.makeText(context, "제작 완료되었습니다.", Toast.LENGTH_SHORT).show()
        Navigation.findNavController(requireView()).popBackStack()
    }

}