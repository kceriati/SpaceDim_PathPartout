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

package com.example.spacedimvisuel.screens.login

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.spacedimvisuel.R
import com.example.spacedimvisuel.databinding.LoginFragmentBinding
import java.util.*


/**
 * Fragment where the game is played
 */
class LoginFragment : Fragment() {


    private lateinit var binding: LoginFragmentBinding
    private val TAG = "LoginFragment"
    private lateinit var viewModel: LoginViewModel

    private var mSensorManager: SensorManager? = null
    private var mAccel = 0f
    private var mAccelCurrent = 0f
    private var mAccelLast = 0f

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.login_fragment,
                container,
                false
        )

        // Specify the current activity as the lifecycle owner.
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        viewModel.userFromAPI.observe(viewLifecycleOwner, Observer {
            Log.i(TAG, viewModel.userFromAPI.value.toString())
            val action = LoginFragmentDirections.actionLoginDestinationToLobbyDestination(viewModel.userFromAPI.value!!)
            //action.user = viewModel.userFromAPI.value!!
            NavHostFragment.findNavController(this).navigate(action)
        })

        binding.rocketButton.setOnClickListener {
            viewModel.findUser(binding.editText.getText().toString())


            //viewModel.joinRoom("FuckThisOkHttpThingyEatMyShit")
        }



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
                    Toast.makeText(getActivity(), "Shake event detected", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        mSensorManager = getActivity()?.getSystemService(Context.SENSOR_SERVICE) as SensorManager;
        Objects.requireNonNull(mSensorManager)?.registerListener(mSensorListener, mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        return binding.root
    }

    /*private fun goToLobby() {
        val action = LoginFragmentDirections.actionLoginDestinationToLobbyDestination()
        NavHostFragment.findNavController(this).navigate(action)
    }*/

}
