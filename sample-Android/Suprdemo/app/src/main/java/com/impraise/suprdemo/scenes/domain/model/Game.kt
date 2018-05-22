package com.impraise.suprdemo.scenes.domain.model

/**
 * Created by guilhermebranco on 3/11/18.
 */
class Game(private val rounds: List<Round>) {

    private var currentRoundIndex = 0
    private val answers = mutableListOf<List<Option>>()
    private val roundsAnswered = mutableListOf<Boolean>()

    init {
        rounds.forEachIndexed{ index, round ->
            answers.add(index, round.options)
            roundsAnswered.add(false)
        }
    }

    val currentState: GameState
        get() {
            return GameState(currentRound = currentRoundIndex + 1,
                    totalRounds = rounds.size,
                    gameOver = isGameOver(),
                    answeredRound = roundsAnswered[currentRoundIndex],
                    score = calculateScore(),
                    currentAnswers = answers[currentRoundIndex])
        }

    fun next(): GameState {
        if (isGameOver()) {
            return currentState
        }

        currentRoundIndex++

        return currentState
    }

    fun answer(option: Option): GameState {
        val round = rounds[currentRoundIndex]
        val list = round.options.map {
            if (it.name == option.name) {
                (it as? Option.Undefined)?.asWrong() ?: it
            } else it
        }
        answers[currentRoundIndex] = list
        roundsAnswered[currentRoundIndex] = true
        return currentState
    }

    private fun isGameOver(): Boolean {
        return currentRoundIndex == rounds.size - 1
    }

    private fun calculateScore(): Score {
        var total = rounds.size
        answers.forEach {
            val wrong = it.filter { it is Option.Wrong }.count()
            total = if (wrong > 0) total - 1 else total
        }
        return Score(total = total)
    }
}

data class GameState(val currentRound: Int = 0,
                     val totalRounds: Int = 0,
                     val gameOver: Boolean = false,
                     val answeredRound: Boolean = false,
                     val score: Score = Score(),
                     val currentAnswers: List<Option> = emptyList()) {

    companion object {
        val EMPTY_GAME = GameState()
    }
}

class Score(val total: Int = 0)