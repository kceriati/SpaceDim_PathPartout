package com.example.spacedimvisuel.api

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: Int,
    val name: String,
    val avatar: String,
    val score: Int,
    val state: State = State.OVER
) :Parcelable

