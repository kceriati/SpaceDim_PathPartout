package com.example.spacedimvisuel.screens.lose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spacedimvisuel.api.User
import com.example.spacedimvisuel.screens.lobby.LobbyViewModel

class LoseViewModelFactory(private val player: User): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoseViewModel::class.java)) {
            return LoseViewModel(player) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
