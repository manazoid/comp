package com.example.comp.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameResult(
    val winner: Boolean,
    val countOfCorrectAnswers: Int,
    val countOfAnswers: Int,
    val gameSettings: GameSettings
) : Parcelable