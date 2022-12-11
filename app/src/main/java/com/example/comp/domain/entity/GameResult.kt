package com.example.comp.domain.entity

import java.io.Serializable

data class GameResult (
    val winner: Boolean,
    val countOfCorrectAnswers: Int,
    val countOfAnswers: Int,
    val gameSettings: GameSettings
        ) : Serializable