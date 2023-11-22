package com.ssafy.smartstore_jetpack.src.main.menu

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
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
import com.ssafy.smartstore_jetpack.util.BeaconSettingUtil
import com.ssafy.smartstore_jetpack.util.RetrofitUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//장바구니 Fragment
private const val TAG = "ShoppingListFragment_싸피"
class ShoppingListFragment(val orderId: Int = -1) : BaseFragment<FragmentShoppingListBinding>(FragmentShoppingListBinding::bind, R.layout.fragment_shopping_list){
    private var shoppingListAdapter : ShoppingListAdapter = ShoppingListAdapter()
    private lateinit var mainActivity: MainActivity
    private var isShop : Boolean = true
    private lateinit var beaconsetting : BeaconSettingUtil

    private val viewModel: MainActivityViewModel by activityViewModels()

    override fun onResume() {
        super.onResume()
        if (!beaconsetting.findBeacon) beaconsetting.startScan()
    }

    override fun onPause() {
        super.onPause()
        beaconsetting.stopScan()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        beaconsetting.destroy()
    }

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

        beaconsetting = mainActivity.beaconsetting

//        mainActivity.setNdef()

        shoppingListAdapter.buttonClickListener = object: ShoppingListAdapter.ButtonClickListener{
            override fun onClick(position: Int) {
                viewModel.deleteFromShoppingList(position)
            }

        }

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

        binding.btnShop.setOnClickListener {
            binding.btnShop.background = ContextCompat.getDrawable(requireContext(), R.drawable.button_color)
            binding.btnTakeout.background = ContextCompat.getDrawable(requireContext(), R.drawable.button_non_color)
            isShop = true
        }
        binding.btnTakeout.setOnClickListener {
            binding.btnTakeout.background = ContextCompat.getDrawable(requireContext(), R.drawable.button_color)
            binding.btnShop.background = ContextCompat.getDrawable(requireContext(), R.drawable.button_non_color)
            isShop = false
        }
        binding.btnOrder.setOnClickListener {
            if(isShop) {
                if (mainActivity.tablenumber < 0) {
                    showDialogForOrderInShop()
                }
                else {
                    completedOrder()
                }
            }
            else {
                //거리가 200이상이라면
                if(true) showDialogForOrderTakeoutOver200m()
            }
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
        viewModel.isOrderCompleted.observe(viewLifecycleOwner){
            if(it) viewModel.clearShoppingList(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivity.hideBottomNav(false)
    }

    private fun showDialogForOrderInShop() {
        val builder: AlertDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle("알림")
            setMessage(
                "Table NFC를 찍어주세요.\n"
            )
            setCancelable(true)
            setNegativeButton("취소"
            ) { dialog, _ -> dialog.cancel() }
        }.create()
        builder.show()
        if (mainActivity.tablenumber > 0) {
            builder.dismiss()
        }
    }
    private fun showDialogForOrderTakeoutOver200m() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("알림")
        builder.setMessage(
            "현재 고객님의 위치가 매장과 200m 이상 떨어져 있습니다.\n정말 주문하시겠습니까?"
        )
        builder.setCancelable(true)
        builder.setPositiveButton("확인") { _, _ ->
            completedOrder()
        }
        builder.setNegativeButton("취소"
        ) { dialog, _ -> dialog.cancel() }
        builder.create().show()
    }

    private fun completedOrder(){

        CoroutineScope(Dispatchers.Main).launch{
            val result = withContext(Dispatchers.IO){
                RetrofitUtil.orderService.makeOrder(
                    Order().apply {
                        userId = ApplicationClass.sharedPreferencesUtil.getUser().id
                        details.addAll(ArrayList(viewModel.shoppingList.value!!))
                        completed = "N"
                        topImg = viewModel.shoppingList.value!![0].img
                        totalQnanty = shoppingListAdapter.itemCount
                        totalPrice = totalAmount()
                        topProductName = viewModel.shoppingList.value!![0].productName
                    }
                )
                true
            }
            viewModel.setOrderCompleted(result)
        }
        Toast.makeText(context,"주문이 완료되었습니다.",Toast.LENGTH_SHORT).show()
        Navigation.findNavController(requireView()).popBackStack()

    }

}