package com.example.spacedimvisuel.old

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.spacedimvisuel.R


class WinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_win)

        val button: Button = findViewById(R.id.neumorphButton)
        button.setOnClickListener {
            val intent = Intent(this@WinActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}


