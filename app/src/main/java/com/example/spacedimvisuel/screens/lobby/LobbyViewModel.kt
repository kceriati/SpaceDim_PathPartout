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

package com.example.spacedimvisuel.screens.lobby

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.spacedimvisuel.MainActivity
import okhttp3.WebSocket
import okhttp3.WebSocketListener

/**
 * ViewModel containing all the logic needed to run the game
 */
class LobbyViewModel : ViewModel() {
    var mainActivityBridge = MainActivity();
    var huh = mainActivityBridge.getLoginVMTraveler().response;
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