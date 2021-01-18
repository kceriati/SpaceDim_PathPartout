/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.spacedimvisuel.screens.login

import android.util.Log
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spacedimvisuel.api.SpaceDimApi
import kotlinx.coroutines.launch

/**
 * ViewModel containing all the logic needed to run the game
 */
class LoginViewModel : ViewModel() {
    // The internal MutableLiveData String that stores the most recent response
    private val _response = MutableLiveData<String>()
    private val TAG = "LoginViewModel"

    // The external immutable LiveData for the response String
    val response: LiveData<String>
        get() = _response

    init {
        Log.i(TAG, "ViewModel Linked")
        getPlayers()
    }

    fun getPlayers() {
        viewModelScope.launch {
            try {
                val listResult = SpaceDimApi.retrofitService.getPlayers()
                _response.value = "Success: ${listResult.size} players retrieved"
            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"
            }
        }
    }

    fun findPlayer(userName: String) {
        viewModelScope.launch {
            try {
                val player = SpaceDimApi.retrofitService.findPlayer(userName)
                _response.value = "Success"
                Log.i(TAG, userName)
                Log.i(TAG, player.id.toString())
            } catch (e: Exception) {
                if(e.message.toString() == "HTTP 404") {
                    createPlayer()
                } else {
                    _response.value = "Failure: ${e.message}"
                    Log.i(TAG, e.message.toString())
                }
            }
        }
    }

    fun createPlayer() {
        viewModelScope.launch {
            try {

            } catch (e: Exception) {

            }
        }
    }
}
