package com.example.spacedimvisuel.api

import android.app.Notification
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.spacedimvisuel.api.PolymorphicAdapter.eventGameParser
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class   SocketListener: WebSocketListener(){

    //current fragment (lobby-> game-> win/lose )
    public val gameState = MutableLiveData<Event>()
    public val gameUi =  MutableLiveData<List<UIElement>>()
    @JsonClass(generateAdapter = true)
    enum class EventType() {
        GAME_STARTED(), GAME_OVER(), ERROR(), READY(), NEXT_ACTION(),
        NEXT_LEVEL(), WAITING_FOR_PLAYER(), PLAYER_ACTION()
    }

    @JsonClass(generateAdapter = true)
    sealed class Event(val type: EventType) {
        data class NextAction(val action: Action) : Event(EventType.NEXT_ACTION)
        data class GameStarted(val uiElementList: List<UIElement>): Event(EventType.GAME_STARTED)
        data class GameOver(val score: Int, val win: Boolean, val level: Int): Event(EventType.GAME_OVER)
        data class NextLevel(val uiElementList: List<UIElement>, val level: Int) : Event(EventType.NEXT_LEVEL)
        data class WaitingForPlayer(val userList: List<User>) : Event(EventType.WAITING_FOR_PLAYER)
        data class Error(val message: String) : Event(EventType.ERROR)
        data class Ready(val value: Boolean) : Event(EventType.READY)
        data class PlayerAction(val uiElement: UIElement): Event(EventType.PLAYER_ACTION)


    }

    @JsonClass(generateAdapter = true)
    enum class UIType {
        BUTTON, SWITCH, SHAKE
    }
    @JsonClass(generateAdapter = true)
    interface IElement {
        var id: Int
        val content: String
    }
    @JsonClass(generateAdapter = true)
    sealed class UIElement(val type: UIType) : IElement {
        data class Button(  override var id: Int, override val content: String) : UIElement(UIType.BUTTON)
        data class Switch(override var id: Int, override val content: String) : UIElement(UIType.SWITCH)
        data class Shake(override var id: Int, override val content: String) : UIElement(UIType.SHAKE)
    }

    @JsonClass(generateAdapter = true)
    data class Action(
        val sentence: String,
        val uiElement: UIElement,
        val time: Long = 8000
    )

    override fun onOpen(webSocket: WebSocket, response: Response)  {
        Log.i("log", "onOpen")
        println("onOpen OK")
        println(response)
    }

    override fun onMessage(webSocket: WebSocket, response: String) {
        Log.i("log", "onMessage")
        println("onMessage")
        println(response)

        var message = eventGameParser.fromJson(response);

        //on envoie les event que l'on reçu dans un livedata
        gameState.postValue(message);
        if (message?.type == EventType.GAME_STARTED) {
            var gamestarted_action = message as SocketListener.Event.GameStarted
            gameUi.postValue(gamestarted_action.uiElementList)
        }

        if (message?.type == EventType.NEXT_LEVEL){
            var gamenextlevel_action = message as SocketListener.Event.NextLevel
            gameUi.postValue(gamenextlevel_action.uiElementList)
        }


    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        println(t.message)
    }


}