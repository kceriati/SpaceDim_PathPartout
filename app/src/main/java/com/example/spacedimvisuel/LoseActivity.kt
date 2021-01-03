package com.example.spacedimvisuel

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import soup.neumorphism.NeumorphCardView
import soup.neumorphism.NeumorphImageButton

import kotlin.math.ln



class LoseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_lose)

        val button: Button = findViewById(R.id.neumorphButton)
        button.setOnClickListener {
            val intent = Intent(this@LoseActivity, LoginActivity::class.java)
            startActivity(intent)
        }



    }
}


