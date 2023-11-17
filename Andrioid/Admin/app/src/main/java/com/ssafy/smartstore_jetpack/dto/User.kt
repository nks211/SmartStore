package com.ssafy.smartstore_jetpack.dto

import android.util.Log

private const val TAG = "User_싸피"


data class User (
    val id: String,
    val name: String,
    val pass: String,
    val stamps: Int,
    val stampList: ArrayList<Stamp> = ArrayList(),
    var isAdmin: Boolean
){

    constructor():this("", "","",0, isAdmin = false)

    constructor(id:String, pass:String):this(id, "",pass, 0, isAdmin = true)

    constructor(id:String, pass:String, admin:Boolean):this(id, "",pass,0, isAdmin = admin)

    constructor(id:String, pass: String, name: String):this(id, name, pass, 0, isAdmin = true)

    constructor(id:String, pass: String, name: String, admin: Boolean):this(id, name, pass, 0, isAdmin = admin)
}