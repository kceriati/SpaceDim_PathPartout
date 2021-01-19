package com.example.spacedimvisuel.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User (
    val id: Long,
    val name: String,
    val avatar: String,
    val score: Long,
    val state: State = State.OVER
)

