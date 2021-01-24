package com.example.spacedimvisuel.screens.win

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spacedimvisuel.api.User
import com.example.spacedimvisuel.screens.lobby.LobbyViewModel

class WinViewModelFactory(private val player: User): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WinViewModel::class.java)) {
            return WinViewModel(player) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}