package com.example.spacedimvisuel.api

data class Player (
    val id: Long,
    val name: String,
    val avatar: String,
    val score: Long,
    val state: String
)