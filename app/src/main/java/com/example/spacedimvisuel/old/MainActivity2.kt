package com.example.spacedimvisuel.old

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.example.spacedimvisuel.R
import soup.neumorphism.NeumorphCardView
import soup.neumorphism.NeumorphImageButton

import kotlin.math.ln



class MainActivity2 : AppCompatActivity() {
    var progressbar_color = 0

    fun my_gradient(value : Int): Int {
        """
           the value should be between 0 and 255 
           with red at 255 
           and green at 0
        """
        var valid_value = value % 255
        val r = 102.212 *ln(0.0742904* valid_value -5.21849)-12.7193
        val g =  38.2662 *  ln(64.7518- 0.324171 * valid_value)+ 95.4575
        val b = 0 +100

       return Color.argb(255,if(r<0) 100 else if(r+100>255) 255 else r.toInt()+100,if(g<0 ) 100 else if(g+100>255) 255 else g.toInt()+100,b.toInt())
    }
    fun color_variation(){
        val progressbar: NeumorphCardView = findViewById(R.id.progressbar)
        progressbar_color+=40
        progressbar.setBackgroundColor(my_gradient(progressbar_color))

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)



        val progressbar: NeumorphCardView = findViewById(R.id.progressbar)
        progressbar.setBackgroundColor(my_gradient(progressbar_color))

        val radio1: RadioButton = findViewById(R.id.radio1)
        val radio2: RadioButton = findViewById(R.id.radio2)


        radio1.setOnClickListener {
            radio2.isChecked = false

        }
        radio2.setOnClickListener {
            radio1.isChecked = false
        }

        val testbutton : NeumorphImageButton = findViewById(R.id.testbutton)
        testbutton.setOnClickListener {
            color_variation()
        }

        val buttonlose: ImageButton = findViewById(R.id.buttonlose)
        buttonlose.setOnClickListener {
            val intent = Intent(this@MainActivity2, LoseActivity::class.java)
            startActivity(intent)
        }
        val buttonwin: ImageButton = findViewById(R.id.buttonwin)
        buttonwin.setOnClickListener {
            val intent = Intent(this@MainActivity2, WinActivity::class.java)
            startActivity(intent)
        }
    }
}



