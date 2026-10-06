package com.example.waracletest.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class CakeDto(
    val title: String,
    val desc: String,
    val image: String,
)
