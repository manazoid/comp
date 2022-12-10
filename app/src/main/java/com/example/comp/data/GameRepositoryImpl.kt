package com.example.comp.data

import com.example.comp.domain.entity.GameSettings
import com.example.comp.domain.entity.Level
import com.example.comp.domain.entity.Question
import com.example.comp.domain.repository.GameRepository
import java.lang.Math.max
import java.lang.Math.min
import kotlin.random.Random

object GameRepositoryImpl : GameRepository {

    private const val MIN_SUM_VALUE = 2
    private const val MIN_ANSWER_VALUE = 1

    override fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question {
        val sum = Random.nextInt(MIN_SUM_VALUE, maxSumValue + 1)
        val visibleNumber = Random.nextInt(1, sum)
        val options = HashSet<Int>()
        val correctAnswer = sum - visibleNumber
        options.add(correctAnswer)
        val from = (correctAnswer - countOfOptions).coerceAtLeast(MIN_ANSWER_VALUE)
        val to = maxSumValue.coerceAtMost(correctAnswer + countOfOptions)
        while (options.size < countOfOptions) {
            options.add(Random.nextInt(from, to))
        }
        return Question(sum, visibleNumber, options.toList())
    }

    override fun getGameSettings(level: Level): GameSettings {
        return when (level) {
            Level.TEST -> {
                GameSettings(
                    10,
                    3,
                    50,
                    8
                )
            }
            Level.EASY -> {
                GameSettings(
                    10,
                    10,
                    70,
                    60
                )
            }
            Level.NORMAL -> {
                GameSettings(
                    20,
                    20,
                    80,
                    40
                )
            }
            Level.HARD -> {
                GameSettings(
                    30,
                    30,
                    90,
                    40
                )
            }
        }
    }
}