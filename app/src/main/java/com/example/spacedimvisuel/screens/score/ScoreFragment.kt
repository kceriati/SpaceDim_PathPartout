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

package com.example.spacedimvisuel.screens.score

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.toColor
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.example.spacedimvisuel.R
import com.example.spacedimvisuel.api.MyWebsocketTraveler
import com.example.spacedimvisuel.api.SocketListener
import com.example.spacedimvisuel.api.State
import com.example.spacedimvisuel.api.User
import com.example.spacedimvisuel.databinding.LobbyFragmentBinding
import com.example.spacedimvisuel.databinding.ScoreFragmentBinding
import com.example.spacedimvisuel.screens.game.GameFragmentArgs
import com.example.spacedimvisuel.screens.game.GameViewModelFactory
import com.example.spacedimvisuel.screens.lobby.LobbyFragmentDirections
import com.google.android.material.color.MaterialColors.getColor
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_container.view.*
import com.example.spacedimvisuel.R.color.light_orange as light_orange1

/**
 * Fragment where the game is played
 */
class ScoreFragment : Fragment() {

    private lateinit var viewModel: ScoreViewModel
    private lateinit var viewModelFactory: ScoreFragmentModelFactory
    private val TAG = "LobbyFragment"

    private lateinit var binding: ScoreFragmentBinding
//    private val args by navArgs<ScoreFragmentArgs>()
    private lateinit var ScoreUserObserver : Observer<List<User>>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.score_fragment,
                container,
                false
        )

        viewModelFactory = ScoreFragmentModelFactory(
            ScoreFragmentArgs.fromBundle(requireArguments()).user
        )

        viewModel = ViewModelProvider(this, viewModelFactory).get(ScoreViewModel::class.java)

        viewModel.currentPlayer

        val gameScoreObserver = Observer<List<User>> { newState ->
            if (newState != null) {
                Log.i(TAG, newState.toString())
                drawUserScore(newState)
            }
        }

        viewModel.userScoreList.observe(viewLifecycleOwner, gameScoreObserver)

        return binding.root
    }

    private fun createPlayerScoreContainer(user: User) :ConstraintLayout{
        val inflater =LayoutInflater.from(this.context)
        val playertile = inflater.inflate(
            R.layout.score_container,
            null,
            false
        ) as ConstraintLayout
        val name= playertile.findViewById<TextView>(R.id.textname)
        val score= playertile.findViewById<TextView>(R.id.textscore)
        Picasso.get().load(user.avatar).into(playertile.userpic)
        name.text = user.name
        score.text = user.score.toString()
        return playertile
    }

    private fun drawUserScore(users : List<User>){
        binding.scoreList.removeAllViews()
        for (user in users){
            binding.scoreList.addView(createPlayerScoreContainer(user))
        }
    }

}