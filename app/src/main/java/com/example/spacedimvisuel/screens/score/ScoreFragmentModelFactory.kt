package com.example.spacedimvisuel.screens.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spacedimvisuel.api.User

class ScoreFragmentModelFactory(private val player: User) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScoreViewModel::class.java)) {
            return ScoreViewModel(player) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}