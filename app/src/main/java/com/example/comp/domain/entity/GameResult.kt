package com.example.comp.domain.entity

data class GameResult (
    val winner: Boolean,
    val countOfCorrectAnswers: Int,
    val countOfAnswers: Int,
    val gameSettings: GameSettings
        )