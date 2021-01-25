package com.example.spacedimvisuel.screens.score

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.spacedimvisuel.api.User

import com.example.spacedimvisuel.api.SocketListener
import com.example.spacedimvisuel.api.SpaceDimApi
import com.example.spacedimvisuel.api.State
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.internal.ws.RealWebSocket

/**
 * ViewModel containing all the logic needed to run the game
 */
class ScoreViewModel(player : User) : ViewModel() {

    val currentPlayer = player
    private val TAG = "ScoreViewModel"

    private val _userScoreList = MutableLiveData<List<User>>()
        init {
            listAllUser()
        }

    // The external immutable LiveData for the response List<User>
    val userScoreList: LiveData<List<User>>
        get() = _userScoreList

    fun listAllUser() {
        viewModelScope.launch {
            try {
                val listUser = mutableListOf<User>()
                   val users = SpaceDimApi.retrofitService.listAllUser()
                    for (user in users){
                        if (listUser.size > 10)
                            break
                val userName = user.name
                val userId = user.id
                val userAvatar = user.avatar
                val userScore = user.score
                        listUser += User(userId, userName, userAvatar, userScore, State.OVER)
                        }
                _userScoreList.postValue(listUser)
            } catch (e: Exception) {
                println(e)
            }
        }
    }



}
