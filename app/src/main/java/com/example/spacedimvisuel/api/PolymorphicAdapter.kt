package com.example.spacedimvisuel.api

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object PolymorphicAdapter {

    private val moshiEventSerializer: Moshi = with(Moshi.Builder()) {
        add(
            PolymorphicJsonAdapterFactory.of(SocketListener.Event::class.java,"type")
                .withSubtype(SocketListener.Event.Ready::class.java, SocketListener.EventType.READY.name)
                .withSubtype(SocketListener.Event.Error::class.java, SocketListener.EventType.ERROR.name)
                .withSubtype(SocketListener.Event.GameOver::class.java, SocketListener.EventType.GAME_OVER.name)
                .withSubtype(SocketListener.Event.GameStarted::class.java, SocketListener.EventType.GAME_STARTED.name)
                .withSubtype(SocketListener.Event.NextAction::class.java, SocketListener.EventType.NEXT_ACTION.name)
                .withSubtype(SocketListener.Event.NextLevel::class.java, SocketListener.EventType.NEXT_LEVEL.name)
                .withSubtype(SocketListener.Event.PlayerAction::class.java, SocketListener.EventType.PLAYER_ACTION.name)
                .withSubtype(SocketListener.Event.WaitingForPlayer::class.java, SocketListener.EventType.WAITING_FOR_PLAYER.name)
        )

        add(
            PolymorphicJsonAdapterFactory.of(SocketListener.UIElement::class.java,"type")
                .withSubtype(SocketListener.UIElement.Button::class.java, SocketListener.UIType.BUTTON.name)
                .withSubtype(SocketListener.UIElement.Shake::class.java, SocketListener.UIType.SHAKE.name)
                .withSubtype(SocketListener.UIElement.Switch::class.java, SocketListener.UIType.SWITCH.name)
        )

        add(KotlinJsonAdapterFactory())
        build()
    }

    val eventGameParser: JsonAdapter<SocketListener.Event> = moshiEventSerializer.adapter(SocketListener.Event::class.java)
}