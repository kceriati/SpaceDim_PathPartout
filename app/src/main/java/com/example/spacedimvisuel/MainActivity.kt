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

package com.example.spacedimvisuel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.spacedimvisuel.api.SocketListener
import com.example.spacedimvisuel.screens.login.LoginViewModel
import okhttp3.*
import java.util.concurrent.TimeUnit

/**
 * Creates an Activity that hosts all of the fragments in the app
 */
class MainActivity : AppCompatActivity() {

    var loginViewModelTraveler = LoginViewModel();

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val client = OkHttpClient.Builder()
            .readTimeout(3, TimeUnit.SECONDS)

            .build()
        val request = Request.Builder()
            .url("ws://spacedim.async-agency.com:8081")
            .build()
        val socketListener = SocketListener()
        val webSocket = client.newWebSocket(request, socketListener)

        client.dispatcher.executorService.shutdown()
    }

    fun getLoginVMTraveler(): LoginViewModel {
        return loginViewModelTraveler;

    }

    private class EchoWebSocketListener : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            webSocket.send("")

        }
    }




}
