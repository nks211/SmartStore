package com.ssafy.smartstore_jetpack.util

import UserService
import com.ssafy.smartstore_jetpack.api.CommentService
import com.ssafy.smartstore_jetpack.api.OrderService
import com.ssafy.smartstore_jetpack.api.ProductService
import com.ssafy.smartstore_jetpack.config.ApplicationClass

class RetrofitUtil {
    companion object{
        val commentService = ApplicationClass.retrofit.create(CommentService::class.java)
        val orderService = ApplicationClass.retrofit.create(OrderService::class.java)
        val productService = ApplicationClass.retrofit.create(ProductService::class.java)
        val userService = ApplicationClass.retrofit.create(UserService::class.java)
    }
}