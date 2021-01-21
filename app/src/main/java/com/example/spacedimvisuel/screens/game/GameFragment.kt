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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.spacedimvisuel.R
import com.example.spacedimvisuel.api.SocketListener
import com.example.spacedimvisuel.databinding.GameFragmentBinding
import com.example.spacedimvisuel.screens.game.UIType.*
import soup.neumorphism.NeumorphCardView
import soup.neumorphism.NeumorphImageButton


/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {

    private lateinit var binding: GameFragmentBinding
    private lateinit var viewModelFactory: GameViewModelFactory
    private lateinit var viewModel: GameViewModel
    //les observers
    private lateinit var gameStateObserver : Observer<SocketListener.Event>
    private lateinit var uiComponentObserver : Observer<List<SocketListener.UIElement>>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_fragment,
                container,
                false
        )

        viewModelFactory = GameViewModelFactory(GameFragmentArgs.fromBundle(requireArguments()).user,GameFragmentArgs.fromBundle(requireArguments()).webSocket)
        viewModel = ViewModelProvider(this, viewModelFactory).get(GameViewModel::class.java)
        //binding.buttonlose.setOnClickListener { nextScreenLose() }
        //binding.buttonwin.setOnClickListener  { nextScreenWin()  }

         gameStateObserver = Observer<SocketListener.Event> { newState ->
            if(newState.type == SocketListener.EventType.GAME_OVER) {
                var gameover_action = newState as SocketListener.Event.GameOver
                if (gameover_action.win) {
                    nextScreenWin()
                }
                else{
                    nextScreenLose()
                }
            }



        }
        viewModel.gameState.observe(viewLifecycleOwner, gameStateObserver)

        uiComponentObserver = Observer<List<SocketListener.UIElement>>{ elements ->
            buildButtons(elements)
        }
        viewModel.gameUiElement.observe(viewLifecycleOwner, uiComponentObserver)

        return binding.root


    }


    private fun nextScreenLose() {
        viewModel.gameState.removeObserver(gameStateObserver)
        val action = GameFragmentDirections.actionGameDestinationToLoseDestination()
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun nextScreenWin() {
        viewModel.gameState.removeObserver(gameStateObserver)
        val action = GameFragmentDirections.actionGameDestinationToWinDestination()
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun buildButtons(elements: List<SocketListener.UIElement>){
        var itemperrow = 2
        var count = 0
        var currentrowindex = 0
        if (elements.size<5){
            binding.table.removeView(binding.row3)
        }
        var listofrow = listOf<TableRow>()
        listofrow += binding.row1
        listofrow += binding.row2
        listofrow += binding.row3
        for (element in elements) {
            if (element.type ==SocketListener.UIType.SWITCH)
                createSwitch(listofrow[currentrowindex],element.id,element.content)
            else if (element.type == SocketListener.UIType.BUTTON)
                createButton(listofrow[currentrowindex],element.id,element.content)
            count++
            if (count >= itemperrow) {
            count = 0;
            currentrowindex++
            }
        }
    }

    private fun createButton(row: TableRow, id: Int, content: String)  {
        val inflater =LayoutInflater.from(this.context)
        val button = inflater.inflate(
                R.layout.button_only,
                row,
                false
        ) as NeumorphImageButton
        //button.setOnClickListener { sendready(viewModel.currentWebSocket) }
        row.addView(button)

    }

    private fun createSwitch(row: TableRow, id: Int, content: String)  {
        val inflater =LayoutInflater.from(this.context)
        val button = inflater.inflate(
                R.layout.switch_only,
                row,
                false
        ) as NeumorphCardView
        //button.setOnClickListener { sendingame(viewModel.currentWebSocket) }
        row.addView(button)
    }


}

enum class UIType {
    BUTTON, SWITCH, SHAKE
}

interface IElement {
    var id: Int
    val content: String
}

sealed class UIElement(val type: UIType) : IElement {
    data class Button(override var id: Int, override val content: String) : UIElement(BUTTON)
    data class Switch(override var id: Int, override val content: String) : UIElement(SWITCH)
    data class Shake(override var id: Int, override val content: String) : UIElement(SHAKE)
}