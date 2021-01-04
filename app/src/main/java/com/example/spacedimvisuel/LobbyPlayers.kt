package com.example.spacedimvisuel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LobbyPlayers : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby_players)
        val button: Button = findViewById(R.id.buttonready)
        button.setOnClickListener {
            val intent = Intent(this@LobbyPlayers, MainActivity::class.java)
            startActivity(intent)
        }
    }
}