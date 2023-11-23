package com.ssafy.smartstore_jetpack.api

import com.ssafy.smartstore_jetpack.dto.Comment
import com.ssafy.smartstore_jetpack.dto.Note
import com.ssafy.smartstore_jetpack.dto.ReComment
import retrofit2.http.*

interface NoteService {

    @POST("rest/note")
    suspend fun insert(@Body note: Note): Boolean

    @PUT("rest/note/{id}")
    suspend fun readNote(@Path("id") id: Int): Boolean

    @DELETE("rest/note/{id}")
    suspend fun delete(@Path("id") id: Int): Boolean

    @GET("rest/note/{id}")
    suspend fun selectAll(@Path("id") id:String): List<Note>

}