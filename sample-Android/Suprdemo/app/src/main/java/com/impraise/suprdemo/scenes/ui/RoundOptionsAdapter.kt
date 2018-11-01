package com.impraise.suprdemo.scenes.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.impraise.suprdemo.R
import com.impraise.suprdemo.scenes.domain.model.Option
import com.impraise.suprdemo.scenes.presentation.GameScene
import com.impraise.suprdemo.scenes.presentation.GameSceneInteraction
import kotlinx.android.synthetic.main.option_item.view.*

class RoundOptionsAdapter(private val gameScene: GameScene) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val options: MutableList<Option> = mutableListOf()

    fun setOptions(newOptions: List<Option>) {
        options.clear()
        options.addAll(newOptions)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return OptionViewHolder(layoutInflater.inflate(R.layout.option_item, parent, false), gameScene)
    }

    override fun getItemCount() = options.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return (holder as OptionViewHolder).bind(options[position])
    }
}

class OptionViewHolder(view: View, private val scene: GameScene) : RecyclerView.ViewHolder(view) {

    private val optionDescription: TextView = view.optionDescription
    private val view: View = view

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