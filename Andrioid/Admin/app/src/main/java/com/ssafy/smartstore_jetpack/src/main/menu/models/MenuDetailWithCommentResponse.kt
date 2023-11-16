package com.ssafy.smartstore_jetpack.src.main.menu.models

import com.google.gson.annotations.SerializedName

data class MenuDetailWithCommentResponse (
    @SerializedName("img") val productImg: String,
    @SerializedName("avg") val productRatingAvg: Double,
    @SerializedName("user_id") val userId: String?,
    @SerializedName("sells") val productTotalSellCnt: Int,
    @SerializedName("price") val productPrice: Int,
    @SerializedName("name") val productName: String,
    @SerializedName("rating") val productRating: Double,
    @SerializedName("commentId") val commentId: Int = -1,
    @SerializedName("comment") val commentContent: String?,
    @SerializedName("userName") val commentUserName: String?,
    @SerializedName("commentCnt") val productCommentTotalCnt: Int,
    @SerializedName("type") val type: String
)
