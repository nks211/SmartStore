package com.ssafy.smartstore_jetpack.src.main.menu

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.config.ApplicationClass
import com.ssafy.smartstore_jetpack.config.BaseFragment
import com.ssafy.smartstore_jetpack.databinding.FragmentOrderBinding
import com.ssafy.smartstore_jetpack.dto.Product
import com.ssafy.smartstore_jetpack.src.main.MainActivity
import com.ssafy.smartstore_jetpack.src.main.MainActivityViewModel
import com.ssafy.smartstore_jetpack.src.main.menu.adapter.MenuAdapter
import com.ssafy.smartstore_jetpack.util.RetrofitUtil
import kotlinx.coroutines.launch

// 하단 주문 탭
private const val TAG = "OrderFragment_싸피"
class MenuFragment : BaseFragment<FragmentOrderBinding>(FragmentOrderBinding::bind, R.layout.fragment_order){
    private var menuAdapter: MenuAdapter = MenuAdapter(arrayListOf())
    private lateinit var mainActivity: MainActivity

    private var isAdmin = -1

    private val activityViewModel:MainActivityViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()

        binding.recyclerViewMenu.apply {
            layoutManager = GridLayoutManager(context,3)
            adapter = menuAdapter
        }

        binding.floatingBtn.setOnClickListener{
            activityViewModel.setProductId(-1)
            mainActivity.openFragment(1)
        }
    }

    private fun initData(){
        lifecycleScope.launch{
            RetrofitUtil.productService.getProductList().let {
                Log.d(TAG, "onSuccess: ${it}")
                menuAdapter.productList = it
                menuAdapter.notifyDataSetChanged()
                menuAdapter.setItemClickListener(object : MenuAdapter.ItemClickListener{
                    override fun onClick(view: View, position: Int, productId:Int) {
                        activityViewModel.setProductId(productId)
                        val salable = if(it[position].isSalable) 1 else 0
                        mainActivity.openFragment(3, "", salable)
                    }
                })
            }
        }
    }
}