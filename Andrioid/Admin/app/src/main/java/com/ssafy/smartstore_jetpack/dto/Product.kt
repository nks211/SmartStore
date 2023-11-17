package com.ssafy.smartstore_jetpack.dto

data class Product (
    val id: Int,
    val name: String,
    val type: String,
    val price: Int,
    val img: String,
    val comment: ArrayList<Comment> = ArrayList()
) {
    constructor(): this(0, "","",0,"")
    constructor(name: String, type: String, price: Int): this(0, name, type, price, "")
}
