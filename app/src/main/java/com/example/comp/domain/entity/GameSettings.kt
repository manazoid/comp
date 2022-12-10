package com.example.comp.domain.entity

data class GameSettings (
    val maxSumValue: Int,
    val minCountOfCorrectAnswers: Int,
    val minPercentOfCorrectAnswers: Int,
    val gameTimeInSeconds: Int
        )