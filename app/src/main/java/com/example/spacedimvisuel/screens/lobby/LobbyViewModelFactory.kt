package com.example.spacedimvisuel.screens.lobby

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spacedimvisuel.api.User

class LobbyViewModelFactory(private val player: User) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LobbyViewModel::class.java)) {
            return LobbyViewModel(player) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}