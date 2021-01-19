package com.example.spacedimvisuel.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserPost(val name: String)