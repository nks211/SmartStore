package com.ssafy.smartstore_jetpack.src.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.smartstore_jetpack.dto.Order
import com.ssafy.smartstore_jetpack.dto.OrderDetail
import com.ssafy.smartstore_jetpack.dto.ShoppingCart
import com.ssafy.smartstore_jetpack.src.main.menu.models.MenuDetailWithCommentResponse
import com.ssafy.smartstore_jetpack.src.main.my.models.LatestOrderResponse
import com.ssafy.smartstore_jetpack.src.main.my.models.OrderDetailResponse
import com.ssafy.smartstore_jetpack.util.CommonUtils
import com.ssafy.smartstore_jetpack.util.RetrofitUtil
import kotlinx.coroutines.launch
import retrofit2.http.Path

private const val TAG = "MainActivityVM_싸피"
class MainActivityViewModel : ViewModel() {

    private val _orderId = MutableLiveData<Int>()
    val orderId: LiveData<Int>
        get() = _orderId

    fun setOrderId(orderId:Int){
        _orderId.value = orderId
    }

    private val _productId = MutableLiveData<Int>()
    val productId: LiveData<Int>
        get() = _productId


    fun setProductId(productId:Int){
        _productId.value = productId
    }

    private val _productInfo = MutableLiveData<List<MenuDetailWithCommentResponse>>()
    val productInfo: LiveData<List<MenuDetailWithCommentResponse>>
        get() = _productInfo

    fun getProductInfo(pId:Int) {
        viewModelScope.launch{
            var info:List<MenuDetailWithCommentResponse>
            try{
                info = RetrofitUtil.productService.getProductWithComments(pId)
            }catch (e:Exception){
                info = arrayListOf()
            }
            _productInfo.value = info
        }
    }

    fun getOrder(o_id: Int): Boolean {
        viewModelScope.launch {
            try{
                _shoppingList.value = CommonUtils.makeOrderDetail(RetrofitUtil.orderService.getOrderDetail(o_id))
            }catch(e: Exception){
                _shoppingList.value = arrayListOf()
            }
        }
        return true
    }

    private val _waitingOrders = MutableLiveData<List<LatestOrderResponse>>()
    val waitingOrders : LiveData<List<LatestOrderResponse>>
        get() = _waitingOrders

    private val _tablenumber = MutableLiveData<Int>()
    val tablenumber : LiveData<Int>
        get() = _tablenumber

    fun setTableNumber(number: Int) {
        _tablenumber.value = number
    }

    private val _shoppingList = MutableLiveData<List<OrderDetail>>()
    val shoppingList: LiveData<List<OrderDetail>>
        get() = _shoppingList

    fun addToShoppingList(detail: OrderDetail){
        if(_shoppingList.value==null)
            _shoppingList.value = arrayListOf()
        val tmp = ArrayList(_shoppingList.value)
        tmp.add(detail)
        _shoppingList.value = tmp
    }

    fun deleteFromShoppingList(position: Int){
        val tmp = ArrayList(_shoppingList.value)
        tmp.removeAt(position)
        _shoppingList.value = tmp
    }

    fun clearShoppingList(finished: Boolean){
        if(finished)
            _shoppingList.value = arrayListOf()
        _isOrderCompleted.value = false
    }

    private val _isOrderCompleted = MutableLiveData<Boolean>()
    val isOrderCompleted: LiveData<Boolean>
            get() = _isOrderCompleted
    fun setOrderCompleted(state: Boolean){
        _isOrderCompleted.value = state
    }




}