package com.example.spacedimvisuel.api

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (
    val id: Long,
    val name: String,
    val avatar: String,
    val score: Long,
    val state: State = State.OVER
) :Parcelable

