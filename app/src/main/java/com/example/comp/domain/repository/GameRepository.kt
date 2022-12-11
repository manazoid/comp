package com.example.comp.domain.repository

import com.example.comp.domain.entity.GameSettings
import com.example.comp.domain.entity.Level
import com.example.comp.domain.entity.Question

interface GameRepository {

    fun generateQuestion(
        maxSumValue: Int,
        countOfOptions: Int
    ): Question

    fun getGameSettings(level: Level): GameSettings
}