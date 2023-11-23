package com.ssafy.smartstore_jetpack.dto

data class Note(
    val id: Int = -1,
    val title: String ="",
    val content: String = "",
    var orderTime: String ="",
    val senderId: String ="",
    val receiverId: String ="",
    var isRead: Boolean = false
)
