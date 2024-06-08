package com.instabug.android_challenge.model

data class Request (
    val url: String,
    val responseBody: String?,
    val requestBody: String?,
    val errorBody: String?,
    val code: Int,
    val executionTime : Long,
    val method: String,
    val status : Boolean = code in 200..299,
    val id: Int = 0,
)