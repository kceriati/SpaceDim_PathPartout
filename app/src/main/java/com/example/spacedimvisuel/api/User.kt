package com.example.spacedimvisuel.api

data class User (
    val id: Long,
    val name: String,
    val avatar: String,
    val score: Long,
    val state: State = State.OVER
)