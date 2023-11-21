package com.ssafy.smartstore_jetpack.dto

data class ReComment(
    val id: Int = -1,
    val commentId: Int = -1,
    var comment: String
){
    constructor(comment_id: Int, comment: String): this(0, comment_id, comment)
}
