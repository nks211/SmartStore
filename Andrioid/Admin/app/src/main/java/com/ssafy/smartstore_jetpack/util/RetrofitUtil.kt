package com.ssafy.smartstore_jetpack.util

import UserService
import com.ssafy.smartstore_jetpack.api.CommentService
import com.ssafy.smartstore_jetpack.api.NoteService
import com.ssafy.smartstore_jetpack.api.OrderService
import com.ssafy.smartstore_jetpack.api.ProductService
import com.ssafy.smartstore_jetpack.config.ApplicationClass

class RetrofitUtil {
    companion object{
        val commentService: CommentService = ApplicationClass.retrofit.create(CommentService::class.java)
        val orderService: OrderService = ApplicationClass.retrofit.create(OrderService::class.java)
        val productService: ProductService = ApplicationClass.retrofit.create(ProductService::class.java)
        val userService: UserService = ApplicationClass.retrofit.create(UserService::class.java)
        val noteService: NoteService = ApplicationClass.retrofit.create(NoteService::class.java)

    }
}