package com.ssafy.smartstore_jetpack.dto

import java.util.Date

data class Note(
    val id: Int = -1,
    val title: String ="",
    val content: String = "",
    var orderTime: Date = Date(),
    val senderId: String ="",
    val receiverId: String ="",
    var isRead: Boolean = false
)
