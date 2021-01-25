/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.spacedimvisuel.screens.game

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.JsonWriter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.graphics.drawable.toDrawable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.NavHostFragment
import com.example.spacedimvisuel.R
import com.example.spacedimvisuel.api.PolymorphicAdapter
import com.example.spacedimvisuel.api.SocketListener
import com.example.spacedimvisuel.api.User
import com.example.spacedimvisuel.databinding.GameFragmentBinding
//import com.example.spacedimvisuel.screens.game.UIType.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.w3c.dom.Text
import soup.neumorphism.NeumorphButton
import soup.neumorphism.NeumorphCardView
import soup.neumorphism.NeumorphImageButton
import java.util.*
import kotlin.math.ln


/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {

    private lateinit var binding: GameFragmentBinding
    private lateinit var viewModelFactory: GameViewModelFactory
    private lateinit var viewModel: GameViewModel

    //les observers
    private lateinit var gameStateObserver: Observer<SocketListener.Event>
    private lateinit var gameUiObserver: Observer<List<SocketListener.UIElement>>

    //color state of progress bar
    private var mSensorManager: SensorManager? = null
    private var mAccel = 0f
    private var mAccelCurrent = 0f
    private var mAccelLast = 0f
    private var currentShakeListenerAction: SocketListener.UIElement? = null

    private var TAG = "GameFragment"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.game_fragment,
            container,
            false
        )

        viewModelFactory = GameViewModelFactory(
            GameFragmentArgs.fromBundle(requireArguments()).user,
            GameFragmentArgs.fromBundle(requireArguments()).webSocket
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(GameViewModel::class.java)
        //binding.buttonlose.setOnClickListener { nextScreenLose() }
        //binding.buttonwin.setOnClickListener  { nextScreenWin()  }

        val valueAnimator = ValueAnimator.ofInt(0, 255)
        valueAnimator.addUpdateListener {

            color_variation(valueAnimator.animatedValue as Int)
        }



        gameStateObserver = Observer<SocketListener.Event> { newState ->
            if (newState.type == SocketListener.EventType.GAME_OVER) {
                var gameover_action = newState as SocketListener.Event.GameOver
                if (gameover_action.win) {
                    nextScreenWin()
                } else {
                    nextScreenLose()
                }
            }

            if (newState.type == SocketListener.EventType.NEXT_LEVEL) {
                var gamenextlevel_action = newState as SocketListener.Event.NextLevel

                buildButtons(gamenextlevel_action.uiElementList)
            }
            if (newState.type == SocketListener.EventType.NEXT_ACTION) {
                var gamenextaction_action = newState as SocketListener.Event.NextAction
                valueAnimator.end()
                valueAnimator.duration = gamenextaction_action.action.time
                valueAnimator.start()
                sendaction(gamenextaction_action.action)
            }
        }
        viewModel.gameState.observe(viewLifecycleOwner, gameStateObserver)


        gameUiObserver = Observer<List<SocketListener.UIElement>> { elements ->
            buildButtons(elements)

        }
        viewModel.gameUi.observe(viewLifecycleOwner, gameUiObserver)

//        nextActionObserver = Observer<SocketListener.Action>{ action ->
//            sendaction(action)
//        }
//        viewModel.gameNextAction.observe(viewLifecycleOwner, nextActionObserver)

        val mSensorListener: SensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                mAccelLast = mAccelCurrent
                mAccelCurrent = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
                val delta = mAccelCurrent - mAccelLast
                mAccel = mAccel * 0.9f + delta
                if (mAccel > 12) {
                    println("je v vomir ")
                    currentShakeListenerAction?.let { sendelementclick(it) }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        mSensorManager = getActivity()?.getSystemService(Context.SENSOR_SERVICE) as SensorManager;
        Objects.requireNonNull(mSensorManager)?.registerListener(
            mSensorListener, mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        );
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        return binding.root
    }


    private fun nextScreenLose() {
        viewModel.gameState.removeObserver(gameStateObserver)
//        viewModel.gameNextAction.removeObserver(nextActionObserver)
//        viewModel.gameUiElement.removeObserver(uiComponentObserver)
        val action = GameFragmentDirections.actionGameDestinationToLoseDestination(viewModel.myPlayer)
        viewModel.currentWebSocket.close(1000,"Game ends");
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun nextScreenWin() {
        viewModel.gameState.removeObserver(gameStateObserver)
//        viewModel.gameNextAction.removeObserver(nextActionObserver)
//        viewModel.gameUiElement.removeObserver(uiComponentObserver)
        val action = GameFragmentDirections.actionGameDestinationToWinDestination(viewModel.myPlayer)
        viewModel.currentWebSocket.close(1000,"Game ends");
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun buildButtons(elements: List<SocketListener.UIElement>) {
        currentShakeListenerAction = null
        var itemperrow = 2
        var count = 0
        binding.table.removeAllViews()


        var currenttablerow = createTablerow()
        for (element in elements) {
            if (element.type == SocketListener.UIType.SWITCH)
                createSwitch(currenttablerow, element)
            else if (element.type == SocketListener.UIType.BUTTON)
                createButton(currenttablerow, element)
            else if (element.type == SocketListener.UIType.SHAKE) {
                createShake(element)
                count--
            }

            count++
            if (count >= itemperrow) {
                count = 0;
                binding.table.addView(currenttablerow)
                currenttablerow = createTablerow()
            }
        }
    }

    private fun createTablerow(): TableRow {
        val inflater = LayoutInflater.from(this.context)
        val table = inflater.inflate(
            R.layout.tablerow,
            binding.table,
            false
        ) as TableRow

        return table


    }

    private fun createButton(row: TableRow, element: SocketListener.UIElement) {
        val inflater = LayoutInflater.from(this.context)
        val button = inflater.inflate(
            R.layout.button_only,
            row,
            false
        ) as NeumorphImageButton
        button.setOnClickListener {
            sendelementclick(element)
            playSound(element.content.toString())

        }
        val content = element.content.toLowerCase()
        when{
            content.contains("café") -> button.setImageResource(R.drawable.cafe)
            content.contains("gaz") -> button.setImageResource(R.drawable.too_much_gaz)
            content.contains("bombe") -> button.setImageResource(R.drawable.rocket_icon)
            content.contains("plaindre") -> button.setImageResource(R.drawable.stop_complaining_crying)
            content.contains("chez") -> button.setImageResource(R.drawable.stay_home)
            content.contains("vie") -> button.setImageResource(R.drawable.quarante_deux)
            content.contains("laser") -> button.setImageResource(R.drawable.laser)
            content.contains("turbine") -> button.setImageResource(R.drawable.turbine)
            else -> button.setImageResource(R.drawable.clouds)
        }
        row.addView(button)

    }

    private fun createSwitch(
        row: TableRow,
        element: SocketListener.UIElement
    ) {
        val inflater = LayoutInflater.from(this.context)
        val switch = inflater.inflate(
            R.layout.switch_only,
            row,
            false
        ) as NeumorphCardView
        val content = element.content
        when {
            content.toLowerCase().contains("hyper") -> switch.findViewById<ImageView>(R.id.image).setImageResource(R.drawable.hyper_prop)
            content.toLowerCase().contains("téléporteur") -> switch.findViewById<ImageView>(R.id.image).setImageResource(R.drawable.teleport)
            content.toLowerCase().contains("gravité") -> switch.findViewById<ImageView>(R.id.image).setImageResource(R.drawable.gravity)
            content.toLowerCase().contains("bouclier") -> switch.findViewById<ImageView>(R.id.image).setImageResource(R.drawable.bouclier2)
            content.toLowerCase().contains("covid") -> switch.findViewById<ImageView>(R.id.image).setImageResource(R.drawable.vaccin)
        }
        switch.findViewById<TextView>(R.id.temptext).text = content
        switch.findViewById<Switch>(R.id.switch1).setOnClickListener {
            sendelementclick(element)
            playSound(element.content.toString())
        }
        row.addView(switch)
    }

    private fun createShake(element: SocketListener.UIElement) {
        currentShakeListenerAction = element
    }

    private fun sendelementclick(element: SocketListener.UIElement) {
        Log.i(
            "yo",
            PolymorphicAdapter.eventGameParser.toJson(SocketListener.Event.PlayerAction(element))
        )
        // viewModel.currentWebSocket.send("{\"type\":\"READY\", \"value\":true}");
        viewModel.currentWebSocket.send(
            PolymorphicAdapter.eventGameParser.toJson(
                SocketListener.Event.PlayerAction(
                    element
                )
            )
        )
    }


    private fun sendaction(action: SocketListener.Action) {
        binding.actionDesc.text = action.sentence
    }

    private fun playSound(desc: String){
        var pSound:MediaPlayer? = null
        when{
            desc.toLowerCase().contains("café") -> pSound = MediaPlayer.create(context, R.raw.coffee)
            desc.toLowerCase().contains("gaz") -> pSound = MediaPlayer.create(context, R.raw.gaz)
            desc.toLowerCase().contains("bombe") -> pSound = MediaPlayer.create(context, R.raw.bombe)
            desc.toLowerCase().contains("plaindre") -> pSound = MediaPlayer.create(context, R.raw.plaindre)
            desc.toLowerCase().contains("chez") -> pSound = MediaPlayer.create(context, R.raw.porte)
            desc.toLowerCase().contains("laser") -> pSound = MediaPlayer.create(context, R.raw.laser)
            desc.toLowerCase().contains("turbine") -> pSound = MediaPlayer.create(context, R.raw.turbine)
            desc.toLowerCase().contains("hyper") -> pSound = MediaPlayer.create(context, R.raw.hyperprop)
            desc.toLowerCase().contains("téléporteur") -> pSound = MediaPlayer.create(context, R.raw.teleporteur)
            desc.toLowerCase().contains("gravité") -> pSound = MediaPlayer.create(context, R.raw.gravity)
            desc.toLowerCase().contains("bouclier") -> pSound = MediaPlayer.create(context, R.raw.bouclier)
            desc.toLowerCase().contains("covid") -> pSound = MediaPlayer.create(context, R.raw.covid)
        }
        pSound?.start()
    }

    fun my_gradient(value: Int): Int {
        """
           the value should be between 0 and 255 
           with red at 255 
           and green at 0
        """
        var valid_value = value % 255
        val r = 102.212 * ln(0.0742904 * valid_value - 5.21849) - 12.7193
        val g = 38.2662 * ln(64.7518 - 0.324171 * valid_value) + 95.4575
        val b = 0 + 100

        return Color.argb(
            255,
            if (r < 0) 100 else if (r + 100 > 255) 255 else r.toInt() + 100,
            if (g < 0) 100 else if (g + 100 > 255) 255 else g.toInt() + 100,
            b.toInt()
        )
    }

    fun color_variation(progressbar_color : Int) {
        val progressbar: NeumorphCardView = binding.progressbar
        progressbar.setBackgroundColor(my_gradient(progressbar_color))

    }
}

