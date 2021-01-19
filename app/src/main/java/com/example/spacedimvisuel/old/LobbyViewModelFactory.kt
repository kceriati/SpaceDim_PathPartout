package com.example.spacedimvisuel.old

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spacedimvisuel.api.User
import com.example.spacedimvisuel.screens.lobby.LobbyViewModel

class LobbyViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LobbyViewModel::class.java)) {
            return LobbyViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}