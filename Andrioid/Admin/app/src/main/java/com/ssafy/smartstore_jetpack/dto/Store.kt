package com.ssafy.smartstore_jetpack.dto

data class Store(
    val id: Int,
    var name: String,
    var tel: String,
    var lat: Double,
    var lng: Double
)