package com.impraise.suprdemo.scenes.domain.model

/**
 * Created by guilhermebranco on 3/11/18.
 */
data class Round(val avatarUrl: String, val options: List<Option>) {

    companion object {
        val INVALID_ROUND = Round("", emptyList())
    }
}

sealed class Option(val name: String) {
    class Correct(name: String): Option(name)
    class Wrong(name: String): Option(name)
    class Undefined(name: String): Option(name) {

        fun asWrong(): Wrong {
            return Wrong(this.name)
        }
    }
}