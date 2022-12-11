package com.example.comp.presentation

import android.app.Application
import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.comp.R
import com.example.comp.data.GameRepositoryImpl
import com.example.comp.domain.entity.GameResult
import com.example.comp.domain.entity.GameSettings
import com.example.comp.domain.entity.Level
import com.example.comp.domain.entity.Question
import com.example.comp.domain.useCases.GenerateQuestionUseCase
import com.example.comp.domain.useCases.GetGameSettingsUseCase

class GameViewModel(
    private val application: Application,
    private val level: Level
) : ViewModel() {

    private lateinit var gameSettings: GameSettings
    private val repository = GameRepositoryImpl

    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private var timer: CountDownTimer? = null

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private val _percentOfCorrectAnswers = MutableLiveData<Int>()
    val percentOfCorrectAnswers: LiveData<Int>
        get() = _percentOfCorrectAnswers

    private val _progressAnswers = MutableLiveData<String>()
    val progressAnswers: LiveData<String>
        get() = _progressAnswers

    private val _enoughCountOfCorrectAnswers = MutableLiveData<Boolean>()
    val enoughCount: LiveData<Boolean>
        get() = _enoughCountOfCorrectAnswers

    private val _enoughPercentOfCorrectAnswers = MutableLiveData<Boolean>()
    val enoughPercent: LiveData<Boolean>
        get() = _enoughPercentOfCorrectAnswers

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    private var countOfCorrectAnswers = 0
    private var countofQuestions = 0

    init {
        startGame()
    }

    private fun startGame() {
        getGameSettings()
        startTimer()
        generateQuestion()
        updateProgress()
    }

    fun chooseAnswer(number: Int) {
        checkAnswer(number)
        updateProgress()
        generateQuestion()
    }

    fun showResult(gameResult: GameResult) {

    }

    private fun updateProgress() {
        val percent = calculatePercentOfCorrectAnswers()
        _percentOfCorrectAnswers.value = percent
        _progressAnswers.value = String.format(
            application.resources.getString(R.string.progress_answers),
            countOfCorrectAnswers,
            gameSettings.minCountOfCorrectAnswers
        )
        _enoughCountOfCorrectAnswers.value =
            countOfCorrectAnswers >= gameSettings.minCountOfCorrectAnswers
        _enoughPercentOfCorrectAnswers.value = percent >= gameSettings.minPercentOfCorrectAnswers
    }

    private fun calculatePercentOfCorrectAnswers(): Int {
        if (countofQuestions == 0) {
            return 0
        }
        return ((countOfCorrectAnswers / countofQuestions.toDouble()) * GameFinishedFragment.PERCENT).toInt()
    }

    private fun checkAnswer(number: Int) {
        val correctAnswer = question.value?.correctAnswer
        if (number == correctAnswer) {
            countOfCorrectAnswers++
        }
        countofQuestions++
    }

    private fun generateQuestion() {
        _question.value = generateQuestionUseCase(gameSettings.maxSumValue)
    }

    private fun getGameSettings() {
        this.gameSettings = getGameSettingsUseCase(level)
        _minPercent.value = gameSettings.minPercentOfCorrectAnswers
    }

    private fun startTimer() {
        timer = object : CountDownTimer(
            gameSettings.gameTimeInSeconds * MILLIS_IN_SECONDS,
            MILLIS_IN_SECONDS
        ) {
            override fun onTick(time: Long) {
                _formattedTime.value = formatTime(time)
            }

            override fun onFinish() {
                finishGame()
            }
        }
        timer?.start()
    }

    private fun formatTime(time: Long): String {
        val seconds = time / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftSeconds = seconds - (minutes * SECONDS_IN_MINUTES)
        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    private fun finishGame() {
        _gameResult.value = GameResult(
            enoughCount.value == true && enoughPercent.value == true,
            countOfCorrectAnswers,
            countofQuestions,
            gameSettings
        )
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    companion object {

        private const val MILLIS_IN_SECONDS = 1000L
        private const val SECONDS_IN_MINUTES = 60L
    }
}