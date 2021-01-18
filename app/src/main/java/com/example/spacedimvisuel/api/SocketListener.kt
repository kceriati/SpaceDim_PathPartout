package com.example.spacedimvisuel.api

import android.util.Log
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class SocketListener: WebSocketListener(){
    override fun onOpen(webSocket: WebSocket, response: Response)  {
        Log.i("log", "onOpen")
        println("onOpen")
        println(response)
    }

    override fun onMessage(webSocket: WebSocket, response: String) {
        Log.i("log", "onMessage")
        println("onMessage")
        println(response)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        println(t.message)
    }
}