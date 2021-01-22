package com.example.spacedimvisuel.api

import okhttp3.WebSocket
import java.io.Serializable

class MyWebsocketTraveler(webSocket: WebSocket,listener: SocketListener) :Serializable {
     var myWebSocket = webSocket
     var myListener = listener
    fun getWebsocket(): WebSocket {  return myWebSocket
    }
    fun getlistener(): SocketListener {  return myListener
    }
}