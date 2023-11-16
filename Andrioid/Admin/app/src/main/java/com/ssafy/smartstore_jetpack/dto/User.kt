package com.ssafy.smartstore_jetpack.dto

data class User (
    val id: String,
    val name: String,
    val pass: String,
    val stamps: Int,
    val stampList: ArrayList<Stamp> = ArrayList()
){
    constructor():this("", "","",0)
    constructor(id:String, pass:String):this(id, "",pass,0)

    constructor(id:String, pass: String, name: String):this(id, name, pass, 0)
}