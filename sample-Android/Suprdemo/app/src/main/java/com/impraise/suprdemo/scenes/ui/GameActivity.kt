package com.impraise.suprdemo.scenes.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import com.impraise.supr.game.scenes.presentation.GameScene
import com.impraise.supr.game.scenes.presentation.GameSceneInteraction
import com.impraise.supr.game.scenes.presentation.model.GameViewModel
import com.impraise.suprdemo.R
import com.impraise.suprdemo.scenes.di.SceneFactory
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

        options.applyAnimation(this)

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
        avatar.visibility = View.GONE
        continueButton.visibility = View.GONE
    }

    private fun showInitialState() {
        start_game_screen.visibility = View.VISIBLE
        loading_screen.visibility = View.GONE
        options.visibility = View.GONE
        result_game_screen.visibility = View.GONE
        avatar.visibility = View.GONE
        continueButton.visibility = View.GONE
    }

    private fun showGame(viewModel: GameViewModel.GameStateViewModel) {
        start_game_screen.visibility = View.GONE
        loading_screen.visibility = View.GONE
        options.visibility = View.VISIBLE
        result_game_screen.visibility = View.GONE
        avatar.visibility = View.VISIBLE

        Picasso.get()
                .load(viewModel.round)
                .into(avatar, object : Callback {

                    override fun onSuccess() {
                        val imageBitmap = ((avatar.drawable) as BitmapDrawable).bitmap
                        val imageDrawable = RoundedBitmapDrawableFactory.create(resources, imageBitmap)
                        imageDrawable.isCircular = true
                        imageDrawable.cornerRadius = Math.max(imageBitmap.width, imageBitmap.height) / 2.0f
                        avatar.setImageDrawable(imageDrawable)
                    }

                    override fun onError(e: Exception?) {
                        Log.e("GameActivity", e?.message.orEmpty())
                    }
                })

        adapter.setOptions(viewModel.options)

        if (viewModel.showContinueButton) {
            continueButton.visibility = View.VISIBLE
        } else {
            continueButton.visibility = View.INVISIBLE
            options.applyAnimation(this)
        }
    }

    private fun showScore(viewModel: GameViewModel.GameOverViewModel) {
        score.text = viewModel.score
        continueButton.visibility = View.GONE
        start_game_screen.visibility = View.GONE
        loading_screen.visibility = View.GONE
        options.visibility = View.GONE
        result_game_screen.visibility = View.VISIBLE
        avatar.visibility = View.GONE
    }

    private fun RecyclerView.applyAnimation(context: Context) {
        val resId = R.anim.layout_animation_bottom_up
        val animation = AnimationUtils.loadLayoutAnimation(context, resId)
        this.layoutAnimation = animation
    }
}
