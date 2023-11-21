package com.ssafy.smartstore_jetpack.api

import com.ssafy.smartstore_jetpack.dto.Comment
import com.ssafy.smartstore_jetpack.dto.ReComment
import retrofit2.http.*

interface CommentService {
    // comment를 추가한다.
    @POST("rest/comment")
    suspend fun insert(@Body comment: Comment): Boolean

    // comment를 수정한다.
    @PUT("rest/comment")
    suspend fun update(@Body comment: Comment): Boolean

    // {id}에 해당하는 comment를 삭제한다.
    @DELETE("rest/comment/{id}")
    suspend fun delete(@Path("id") id: Int): Boolean

    @GET("rest/comment/reComment/{c_id}")
    suspend fun getReComment(@Path("c_id") c_id: Int): ReComment

    @POST("rest/comment/reComment")
    suspend fun insertReComment(@Body reComment: ReComment): Boolean

}