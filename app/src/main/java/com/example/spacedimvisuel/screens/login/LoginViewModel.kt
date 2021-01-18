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
import com.example.spacedimvisuel.api.Player
import com.example.spacedimvisuel.api.SpaceDimApi
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

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
        Log.i(TAG, "test de connexion")
        SpaceDimApi.retrofitService.getPlayers().enqueue(
            object: Callback<List<Player>> {
                override fun onFailure(call: Call<List<Player>>, t: Throwable) {
                    _response.postValue("Failure: " + t.message)
                    Log.i(TAG, response.toString())
                }

                override fun onResponse(
                    call: Call<List<Player>>,
                    response: Response<List<Player>>
                ) {
                    _response.postValue("Success: ${response.body()?.size} players")
                    Log.i(TAG, response.toString())
                }
            })
    }

    fun joinRoom(roomName:String){
        //OKHTTP
        val client = OkHttpClient()
        val request = Request.Builder().url("ws://spacedim.async-agency.com:8081/ws/join/" + roomName + "/1").build();

        //WBS
        val listener = SocketListener()
        val webSocket = client.newWebSocket(request, listener)

        //REQ
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response){
                if(!response.isSuccessful) throw IOException("Unexpected code $response")

                //TODO: REDIRECTION
            }
        })
    }
}


class SocketListener: WebSocketListener(){
    override fun onOpen(webSocket: WebSocket, response: okhttp3.Response)  {
        Log.i("log", "onOpen")
        println("onOpen")
        println(response)
    }

    override fun onMessage(webSocket: WebSocket, response: String) {
        Log.i("log", "onMessage")
        println("onMessage")
        println(response)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
        super.onFailure(webSocket, t, response)
        println(t.message)
    }
}