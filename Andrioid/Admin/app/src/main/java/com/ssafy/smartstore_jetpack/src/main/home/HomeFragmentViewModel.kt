package com.ssafy.smartstore_jetpack.src.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.smartstore_jetpack.src.main.my.models.LatestOrderResponse
import com.ssafy.smartstore_jetpack.util.RetrofitUtil
import kotlinx.coroutines.launch

class HomeFragmentViewModel: ViewModel() {
    private val _userLastOrderData = MutableLiveData<List<LatestOrderResponse>>()
    val userLastOrderData: LiveData<List<LatestOrderResponse>>
        get() = _userLastOrderData

    fun getListOrderData(id:String){
        viewModelScope.launch{
            try{
                _userLastOrderData.value = RetrofitUtil.orderService.getLastMonthOrder(id)
            }catch(e:Exception){
                _userLastOrderData.value = arrayListOf()
            }
        }
    }
}