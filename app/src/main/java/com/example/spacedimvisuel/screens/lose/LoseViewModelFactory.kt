package com.example.spacedimvisuel.screens.lose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spacedimvisuel.api.User

class LoseViewModelFactory(private val player: User, private val scoreFinal: Int): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoseViewModel::class.java)) {
            return LoseViewModel(player, scoreFinal) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
