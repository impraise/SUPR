package com.impraise.suprdemo.scenes.ui

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.impraise.supr.game.scenes.presentation.GameScene
import com.impraise.supr.game.scenes.presentation.GameSceneInteraction
import com.impraise.suprdemo.R
import com.impraise.suprdemo.scenes.domain.model.Option
import kotlinx.android.synthetic.main.option_item.view.*

class RoundOptionsAdapter(private val gameScene: GameScene) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    private val options: MutableList<Option> = mutableListOf()

    fun setOptions(newOptions: List<Option>) {
        options.clear()
        options.addAll(newOptions)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return OptionViewHolder(layoutInflater.inflate(R.layout.option_item, parent, false), gameScene)
    }

    override fun getItemCount() = options.size

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        return (holder as OptionViewHolder).bind(options[position])
    }
}

class OptionViewHolder(private val view: View, private val scene: GameScene) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

    private val optionDescription: TextView = view.optionDescription

    fun bind(option: Option) {
        optionDescription.text = option.name
        optionDescription.setBackgroundResource(option.background())
        view.setOnClickListener {
            scene.onInteraction(GameSceneInteraction.Answer(option.name))
        }
    }
}

private fun Option.background(): Int {
    return when (this) {
        is Option.Correct -> R.drawable.game_option_correct_bg
        is Option.Undefined -> R.drawable.game_option_normal_bg
        is Option.Wrong -> R.drawable.game_option_wrong_bg
    }
}