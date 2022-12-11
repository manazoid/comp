package com.example.comp.domain.entity

import java.io.Serializable

data class GameSettings (
    val maxSumValue: Int,
    val minCountOfCorrectAnswers: Int,
    val minPercentOfCorrectAnswers: Int,
    val gameTimeInSeconds: Int
        ) : Serializable