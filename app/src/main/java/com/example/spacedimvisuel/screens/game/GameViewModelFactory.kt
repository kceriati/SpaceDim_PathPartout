package com.example.spacedimvisuel.screens.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spacedimvisuel.api.MyWebsocketTraveler
import com.example.spacedimvisuel.api.User

class GameViewModelFactory(private val player: User, private val webSocketTraveler: MyWebsocketTraveler) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(player,webSocketTraveler) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}