package com.ssafy.smartstore_jetpack.src.main.menu

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.config.ApplicationClass
import com.ssafy.smartstore_jetpack.config.BaseFragment
import com.ssafy.smartstore_jetpack.databinding.FragmentOrderBinding
import com.ssafy.smartstore_jetpack.dto.Product
import com.ssafy.smartstore_jetpack.src.main.MainActivity
import com.ssafy.smartstore_jetpack.src.main.MainActivityViewModel
import com.ssafy.smartstore_jetpack.src.main.home.HomeFragmentDirections
import com.ssafy.smartstore_jetpack.src.main.menu.adapter.MenuAdapter
import com.ssafy.smartstore_jetpack.util.RetrofitUtil
import kotlinx.coroutines.launch

// 하단 주문 탭
private const val TAG = "OrderFragment_싸피"
class MenuFragment : BaseFragment<FragmentOrderBinding>(FragmentOrderBinding::bind, R.layout.fragment_order){
    private var menuAdapter: MenuAdapter = MenuAdapter()
    private lateinit var mainActivity: MainActivity

    private var isAdmin = -1

    private val activityViewModel:MainActivityViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated: aa")

        val items = resources.getStringArray(R.array.menu_state)

//        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)

        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, items)
        binding.stateSelect.adapter = spinnerAdapter
        binding.stateSelect.onItemSelectedListener =
            object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                    when(pos){
                        0->{
                            menuAdapter.submitList(activityViewModel.menus.value)
                        }
                        1->{
                            val filteredList = activityViewModel.menus.value!!.filter {
                                it.isSalable
                            }
                            menuAdapter.submitList(filteredList)
                        }
                        else->{
                            val filteredList = activityViewModel.menus.value!!.filter {
                                !it.isSalable
                            }
                            menuAdapter.submitList(filteredList)
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }

        initData()

        binding.recyclerViewMenu.apply {
            layoutManager = GridLayoutManager(context,3)
            adapter = menuAdapter
        }

        binding.floatingBtn.setOnClickListener{
            activityViewModel.setProductId(-1)
            Navigation.findNavController(view).navigate(R.id.menuAddFragment)
        }
    }

    private fun initData(){
        lifecycleScope.launch{
            RetrofitUtil.productService.getProductList().let {
                Log.d(TAG, "onSuccess: ${it}")
                activityViewModel.setMenus(it)
                menuAdapter.submitList(it)
                menuAdapter.setItemClickListener(object : MenuAdapter.ItemClickListener{
                    override fun onClick(view: View, position: Int, productId:Int) {
                        activityViewModel.setProductId(productId)
                        val salable = if(it[position].isSalable) 1 else 0
                        activityViewModel.setProductSalable(it[position].isSalable)
//                        val action = MenuFragmentDirections.actionMenuFragmentToMenuDetailFragment(it[position].isSalable)
//                        Navigation.findNavController(requireView()).navigate(action)
                        Navigation.findNavController(requireView()).navigate(R.id.menuDetailFragment)
//                        mainActivity.openFragment(3, "", salable)
                    }
                })
            }
        }
    }
}