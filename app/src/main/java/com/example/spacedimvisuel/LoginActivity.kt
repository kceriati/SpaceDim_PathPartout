package com.example.spacedimvisuel

import android.graphics.Color
import android.os.Bundle
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import soup.neumorphism.NeumorphCardView
import soup.neumorphism.NeumorphImageButton

import kotlin.math.ln

class LoginActivity : AppCompatActivity() {
    var progressbar_color = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

    }
}



