package com.example.comp.domain.useCases

import com.example.comp.domain.entity.GameSettings
import com.example.comp.domain.entity.Level
import com.example.comp.domain.repository.GameRepository

class GetGameSettingsUseCase(
    private val repository: GameRepository
) {

    operator fun invoke(level: Level): GameSettings {
        return repository.getGameSettings(level)
    }
}