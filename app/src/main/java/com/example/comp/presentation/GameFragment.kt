package com.example.comp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.comp.R
import com.example.comp.data.GameRepositoryImpl
import com.example.comp.databinding.FragmentGameBinding
import com.example.comp.domain.entity.GameResult
import com.example.comp.domain.entity.GameSettings
import com.example.comp.domain.entity.Level
import com.example.comp.domain.repository.GameRepository

class GameFragment : Fragment() {

    private lateinit var level: Level
    private lateinit var gameSettings: GameSettings
    private lateinit var gameRepositoryImpl: GameRepository

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    private fun setupGameSettings() {
        gameSettings = gameRepositoryImpl.getGameSettings(level)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameRepositoryImpl = GameRepositoryImpl
        setupGameSettings()
        with(binding) {
            tvOption1.setOnClickListener {
                launchFinishedGame(
                    GameResult(
                        true,
                        10,
                        10,
                        gameSettings
                    )
                )
            }
        }
    }

    private fun launchFinishedGame(gameResult: GameResult) {
        with(requireActivity().supportFragmentManager) {
            beginTransaction()
                .replace(R.id.main_container, GameFinishedFragment.newInstance(gameResult))
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs() {
        requireArguments().getParcelable<Level>(KEY_LEVEL)?.let {
            level = it
        }
    }

    companion object {

        const val NAME = "GameFragment"
        private const val KEY_LEVEL = "level"

        fun newInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_LEVEL, level)
                }
            }
        }
    }
}
