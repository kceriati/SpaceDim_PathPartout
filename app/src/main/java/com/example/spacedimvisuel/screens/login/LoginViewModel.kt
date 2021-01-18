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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spacedimvisuel.api.SpaceDimApi
import com.example.spacedimvisuel.api.UserPost
import com.squareup.moshi.Moshi
import kotlinx.coroutines.launch

/**
 * ViewModel containing all the logic needed to run the game
 */
class LoginViewModel : ViewModel() {
    // The internal MutableLiveData String that stores the most recent response
    private val _response = MutableLiveData<String>()
    private val moshi = Moshi.Builder().build()
    private val TAG = "LoginViewModel"

    // The external immutable LiveData for the response String
    val response: LiveData<String>
        get() = _response

    init {
        Log.i(TAG, "ViewModel Linked")
    }

    fun findUser(userName: String) {
        viewModelScope.launch {
            try {
                val user = SpaceDimApi.retrofitService.findUser(userName)
                val userId = user.id.toInt()
                logUser(userId)
            } catch (e: Exception) {
                println(e)
                createUser(userName)
            }
        }
    }

    fun logUser(userId: Int) {
        viewModelScope.launch {
            try {
                val user = SpaceDimApi.retrofitService.logUser(userId)
            } catch (e: Exception) {
                Log.i(TAG, e.message.toString())
            }
        }
    }

    fun createUser(userName: String) {
        viewModelScope.launch {
            try {
                val newUser = UserPost(userName)
                val service = SpaceDimApi.retrofitService.createUser(newUser)

            } catch (e: Exception) {
                //HttpException()
                Log.i(TAG, e.message.toString())
            }
        }
    }
}