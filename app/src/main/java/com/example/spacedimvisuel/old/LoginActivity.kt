package com.example.spacedimvisuel.old

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.spacedimvisuel.R

class LoginActivity : AppCompatActivity() {
    var progressbar_color = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)


        val button: ImageButton = findViewById(R.id.fusebutton)
        button.setOnClickListener {
            val intent = Intent(this@LoginActivity, LobbyEmpty::class.java)
            startActivity(intent)
        }
    }
}



