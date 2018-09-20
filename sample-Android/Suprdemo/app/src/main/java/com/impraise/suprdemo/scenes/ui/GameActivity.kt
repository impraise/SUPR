package com.impraise.suprdemo.scenes.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.impraise.suprdemo.R
import com.impraise.suprdemo.scenes.di.SceneFactory
import com.impraise.suprdemo.scenes.presentation.GameScene
import com.impraise.suprdemo.scenes.presentation.GameSceneInteraction
import com.impraise.suprdemo.scenes.presentation.model.GameViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_game.start_game_btn as startGameButton
import kotlinx.android.synthetic.main.activity_game.continue_btn as continueButton
import kotlinx.android.synthetic.main.activity_game.new_game_btn as newGameButton
import kotlinx.android.synthetic.main.activity_game.*
import java.lang.Exception

class GameActivity : AppCompatActivity() {

    private lateinit var scene: GameScene
    private lateinit var adapter: RoundOptionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        scene = ViewModelProviders.of(this, SceneFactory()).get(GameScene::class.java)

        adapter = RoundOptionsAdapter(scene).also {
            options.adapter = it
            options.layoutManager = LinearLayoutManager(this)
        }

        scene.gamePresenter.viewModelStream.observe(this, Observer { viewModelEntity ->
            viewModelEntity?.let { viewModel ->
                when (viewModel) {
                    is GameViewModel.GameNotStartedViewModel -> showInitialState()
                    is GameViewModel.LoadingViewModel -> loading()
                    is GameViewModel.GameStateViewModel -> showGame(viewModel)
                    is GameViewModel.GameOverViewModel -> showScore(viewModel)
                }
            }
        })

        scene.onInteraction(GameSceneInteraction.OnLoad())

        startGameButton.setOnClickListener {
            scene.onInteraction(GameSceneInteraction.StartGame())
        }

        continueButton.setOnClickListener {
            scene.onInteraction(GameSceneInteraction.Continue())
        }

        newGameButton.setOnClickListener {
            scene.onInteraction(GameSceneInteraction.OnLoad())
        }
    }

    private fun loading() {
        loading_screen.visibility = View.VISIBLE
        start_game_screen.visibility = View.GONE
        options.visibility = View.GONE
        result_game_screen.visibility = View.GONE
    }

    private fun showInitialState() {
        start_game_screen.visibility = View.VISIBLE
        loading_screen.visibility = View.GONE
        options.visibility = View.GONE
        result_game_screen.visibility = View.GONE
        countdownTimerLayout.visibility = View.GONE
        avatar.visibility = View.GONE
        continueButton.visibility = View.GONE
    }

    private fun showGame(viewModel: GameViewModel.GameStateViewModel) {
        start_game_screen.visibility = View.GONE
        loading_screen.visibility = View.GONE
        options.visibility = View.VISIBLE
        result_game_screen.visibility = View.GONE
        countdownTimerLayout.visibility = View.VISIBLE
        avatar.visibility = View.VISIBLE

        Picasso.get()
                .load(viewModel.round)
                .fetch(object : Callback {

                    override fun onSuccess() {
                        Picasso.get()
                                .load(viewModel.round)
                                .placeholder(R.drawable.no_person)
                                .error(R.drawable.no_person)
                                .into(avatar)
                    }

                    override fun onError(e: Exception?) {
                        Log.e("GameActivity", e?.message.orEmpty())
                    }
                })

        adapter.setOptions(viewModel.options)

        continueButton.visibility = if (viewModel.showContinueButton) View.VISIBLE else View.INVISIBLE
    }

    private fun showScore(viewModel: GameViewModel.GameOverViewModel) {
        score.text = viewModel.score
        continueButton.visibility = View.GONE
        start_game_screen.visibility = View.GONE
        loading_screen.visibility = View.GONE
        options.visibility = View.GONE
        result_game_screen.visibility = View.VISIBLE
        countdownTimerLayout.visibility = View.GONE
        avatar.visibility = View.GONE
    }
}
