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

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.spacedimvisuel.R
import com.example.spacedimvisuel.databinding.LoginFragmentBinding


/**
 * Fragment where the game is played
 */
class LoginFragment : Fragment() {


    private lateinit var binding: LoginFragmentBinding
    private val TAG = "LoginFragment"
    private lateinit var viewModel: LoginViewModel

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
            // TODO remettre l'action avant de commit
            val action = LoginFragmentDirections.actionLoginDestinationToLobbyDestination(viewModel.userFromAPI.value!!)
             //val action = LoginFragmentDirections.actionLoginDestinationToScoreDestination(viewModel.userFromAPI.value!!)
            //action.user = viewModel.userFromAPI.value!!
            NavHostFragment.findNavController(this).navigate(action)
        })

        binding.rocketButton.setOnClickListener {
            viewModel.findUser(binding.editText.getText().toString())


            //viewModel.joinRoom("FuckThisOkHttpThingyEatMyShit")
        }


        return binding.root
    }

    /*private fun goToLobby() {
        val action = LoginFragmentDirections.actionLoginDestinationToLobbyDestination()
        NavHostFragment.findNavController(this).navigate(action)
    }*/

}