package com.ssafy.smartstore_jetpack.api

import com.ssafy.smartstore_jetpack.dto.Product
import com.ssafy.smartstore_jetpack.src.main.menu.models.MenuDetailWithCommentResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path

interface ProductService {

    @Multipart
    @POST("rest/product")
    suspend fun addProduct(@Part img:MultipartBody.Part, @PartMap product: HashMap<String, RequestBody>): Boolean

    // 전체 상품의 목록을 반환한다
    @GET("rest/product")
    suspend fun getProductList(): List<Product>

    // {productId}에 해당하는 상품의 정보를 comment와 함께 반환한다.
    // comment 조회시 사용
    @GET("rest/product/{productId}")
    suspend fun getProductWithComments(@Path("productId") productId: Int): List<MenuDetailWithCommentResponse>

    @PUT("rest/product")
    suspend fun updateProduct(@Body body: Product): Boolean

    @Multipart
    @PUT("rest/product/includeImg")
    suspend fun updateProductWithImg(@Part img:MultipartBody.Part, @PartMap product: HashMap<String, RequestBody>): Boolean

}