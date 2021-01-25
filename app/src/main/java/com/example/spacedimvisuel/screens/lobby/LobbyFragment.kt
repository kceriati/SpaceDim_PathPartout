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

package com.example.spacedimvisuel.screens.lobby

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toDrawable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.spacedimvisuel.R
import com.example.spacedimvisuel.api.MyWebsocketTraveler
import com.example.spacedimvisuel.api.SocketListener
import com.example.spacedimvisuel.api.State
import com.example.spacedimvisuel.api.User
import com.example.spacedimvisuel.databinding.LobbyFragmentBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_container.view.*

/**
 * Fragment where the game is played
 */
class LobbyFragment : Fragment() {

    private lateinit var viewModel: LobbyViewModel
    private lateinit var viewModelFactory: LobbyViewModelFactory
    private val  listPlayer = {"p1";"p2"}
    private val TAG = "LobbyFragment"

    private lateinit var binding: LobbyFragmentBinding
    private lateinit var lobbyUserObserver : Observer<List<User>>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.lobby_fragment,
                container,
                false
        )

      //  viewModelFactory = LobbyViewModelFactory(LobbyFragmentArgs.fromBundle(arguments!!).user)

        binding.button.setOnClickListener {drawRoomName()}



        viewModelFactory = LobbyViewModelFactory(LobbyFragmentArgs.fromBundle(requireArguments()).user)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LobbyViewModel::class.java)
        //println("REPONSE REUSSIE : " + this.viewModel.mainActivityBridge.getLoginVMTraveler())
       /* println("REPONSE REUSSIE : " + this.viewModel.mainActivityBridge.getLoginVMTraveler())*/




        val gameStarterObserver = Observer<SocketListener.Event> { newState ->
            if (newState.type == SocketListener.EventType.WAITING_FOR_PLAYER) {
                var lobbywaitforplayer_action = newState as SocketListener.Event.WaitingForPlayer
                drawUsers(lobbywaitforplayer_action.userList)
                if (lobbywaitforplayer_action.userList.size > 1) {
                    var alluserrdy = true
                    for (user in lobbywaitforplayer_action.userList) {
                        if (user.state == State.WAITING) {
                            alluserrdy = false
                        }
                    }
                    if (alluserrdy) {
                        println("jenaviguedeouf")
                        val action =
                            LobbyFragmentDirections.actionLobbyDestinationToGameDestination(
                                viewModel.currentPlayer,
                                MyWebsocketTraveler(viewModel.webSocket!!, viewModel.listener)
                            )
                        //action.user = viewModel.userFromAPI.value!!
                        NavHostFragment.findNavController(this).navigate(action)

                    }
                }

            }


           /* if (newState.type == SocketListener.EventType.GAME_STARTED ) {
                val action = LobbyFragmentDirections.actionLobbyDestinationToGameDestination(
                    viewModel.currentPlayer,
                    MyWebsocketTraveler(viewModel.webSocket!!, viewModel.listener)
                )
                //action.user = viewModel.userFromAPI.value!!
                NavHostFragment.findNavController(this).navigate(action)
            }*/
        }
        viewModel.gameState.observe(viewLifecycleOwner, gameStarterObserver)

        return binding.root
    }
//    private fun nextScreen() {
//        val action = LobbyFragmentDirections.actionLobbyDestinationToGameDestination()
//        NavHostFragment.findNavController(this).navigate(action)
//    }

    private fun createPlayerContainer(user: User) :ConstraintLayout{
        val inflater =LayoutInflater.from(this.context)
        val playertile = inflater.inflate(
            R.layout.user_container,
            null,
            false
        ) as ConstraintLayout
        //playertile.setOnClickListener { toggle(id) }
        val name = playertile.findViewById<TextView>(R.id.username)
        val score = playertile.findViewById<TextView>(R.id.score)
        Picasso.get().load(user.avatar).into(playertile.userpic)
        if (user.state == State.WAITING) {
            playertile.statuscontainer.setBackgroundResource(R.color.light_orange.toInt())
            playertile.status.text = "WAITING"
        }
        else if(user.state == State.READY){
            playertile.statuscontainer.setBackgroundResource(R.color.design_default_color_secondary.toInt())
            playertile.status.text = "READY"
        }

        name.text = user.name
        score.text = user.score.toString()
        return playertile
    }

    private fun drawUsers(users : List<User>){
        binding.playerList.removeAllViews()
       for (user in users){
           binding.playerList.addView(createPlayerContainer(user))
       }
    }

    private fun drawRoomName(){
        val builder = AlertDialog.Builder(this.requireContext())
        val inflater = layoutInflater
        builder.setTitle("Please enter room name")
        val dialogLayout = inflater.inflate(R.layout.alert_dialog_room, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.roomNameEditText)
        builder.setView(dialogLayout)
        var roomName = ""
        builder.setPositiveButton("OK") { dialog, which ->
            roomName = editText.text.toString()
            viewModel.joinRoom(roomName,viewModel.currentPlayer)
            binding.button.setOnClickListener {
                viewModel.sendready()
                binding.button.text ="READY ! "
                //la couleur est à check c chelou
                binding.button.background = R.color.design_default_color_secondary.toDrawable()
            }
            binding.button.setBackgroundColor(R.color.design_default_color_secondary.toInt())
            binding.button.text ="READY ? "
            binding.imageView.setImageResource(R.drawable.happyface)

        }
        builder.show()
    }

}