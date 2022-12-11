package com.example.comp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.comp.R
import com.example.comp.databinding.FragmentGameFinishedBinding
import com.example.comp.domain.entity.GameResult
import com.example.comp.domain.entity.GameSettings

class GameFinishedFragment : Fragment() {

    private lateinit var gameResult: GameResult
    private lateinit var gameSettings: GameSettings

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    private fun parseArgs() {
        requireArguments().getParcelable<GameResult>(KEY_GAME_RESULT)?.let {
            gameResult = it
        }
        gameSettings = gameResult.gameSettings
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            emojiResult.setImageResource(getSmileResId())
            tvRequiredAnswers.text = String.format(
                getString(R.string.required_score),
                gameSettings.minCountOfCorrectAnswers
            )

            tvRequiredPercentage.text = String.format(
                getString(R.string.required_percentage),
                gameSettings.minPercentOfCorrectAnswers
            )

            tvScoreAnswers.text = String.format(
                getString(R.string.score_answers),
                gameResult.countOfCorrectAnswers
            )

            tvScorePercentage.text = String.format(
                getString(R.string.score_percentage),
                percentResult()
            )
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                retryGame()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        binding.buttonRetry.setOnClickListener {
            retryGame()
        }
    }

    private fun getSmileResId(): Int {
        return if (gameResult.winner) {
            R.drawable.ic_smile
        } else {
            R.drawable.ic_sad
        }
    }

    private fun percentResult() = with(gameResult) {
        if (countOfAnswers == 0) {
            0
        } else {
            ((countOfCorrectAnswers / countOfAnswers.toDouble()) * PERCENT).toInt()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retryGame() {
        requireActivity().supportFragmentManager.popBackStack(
            GameFragment.NAME,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    companion object {

        const val PERCENT = 100
        private const val KEY_GAME_RESULT = "result"

        fun newInstance(gameResult: GameResult): GameFinishedFragment {
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_GAME_RESULT, gameResult)
                }
            }
        }
    }
}
