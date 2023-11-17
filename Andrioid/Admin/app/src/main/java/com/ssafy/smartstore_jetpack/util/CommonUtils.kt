package com.ssafy.smartstore_jetpack.util

import android.util.Log
import com.ssafy.smartstore_jetpack.config.ApplicationClass
import com.ssafy.smartstore_jetpack.dto.OrderDetail
import com.ssafy.smartstore_jetpack.src.main.my.models.LatestOrderResponse
import com.ssafy.smartstore_jetpack.src.main.my.models.OrderDetailResponse
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "CommonUtils_싸피"
object CommonUtils {

    //천단위 콤마
    fun makeComma(num: Int): String {
        var comma = DecimalFormat("#,###")
        return "${comma.format(num)} 원"
    }

    fun getFormattedString(date:Date): String {
        val dateFormat = SimpleDateFormat("yyyy.MM.dd HH시 mm분")
        dateFormat.timeZone = TimeZone.getTimeZone("Seoul/Asia")

        return dateFormat.format(date)
    }

    // 시간 계산을 통해 완성된 제품인지 확인
    fun isOrderCompleted(orderDetail: OrderDetailResponse): String {
        return if( checkTime(orderDetail.orderDate.time))  "주문완료" else "진행 중.."
    }

    // 시간 계산을 통해 완성된 제품인지 확인
    fun isOrderCompleted(order: LatestOrderResponse): String {
        return if( checkTime(order.orderDate.time))  "주문완료" else "진행 중.."
    }

    private fun checkTime(time:Long):Boolean{
        val curTime = (Date().time+60*60*9*1000)

        return (curTime - time) > ApplicationClass.ORDER_COMPLETED_TIME
    }

    // 최근 주문 목록에서 총가격, 주문 개수 구하여 List로 반환한다.
    // 반환되는 List의 경우 화면에서 보여주는 최근 주문 목록 List이다.
    fun makeLatestOrderList(latestOrderList: List<LatestOrderResponse>): List<LatestOrderResponse>{
        val hm = HashMap<Int, LatestOrderResponse>()
        latestOrderList.forEach { order ->
            if(hm.containsKey(order.orderId)){
                val tmp = hm[order.orderId]!!
                tmp.orderCnt += order.orderCnt
                tmp.totalPrice  += order.productPrice * order.orderCnt
                hm[order.orderId] = tmp
            }
            else {
                order.totalPrice = order.productPrice * order.orderCnt
                hm[order.orderId] = order
            }
        }
        val list = ArrayList(hm.values)
        list.sortWith { o1, o2 -> o2.orderDate.compareTo(o1.orderDate) }
        return list
    }

    fun makeOrderDetail(lst: List<OrderDetailResponse>): List<OrderDetail>{
        val list = ArrayList<OrderDetail>()
        lst.forEach{ order->
            list.add(OrderDetail(order.productId, order.quantity).apply{
                img = order.img
                productName = order.productName
                unitPrice = order.unitPrice
            })
        }
        return list
    }
}