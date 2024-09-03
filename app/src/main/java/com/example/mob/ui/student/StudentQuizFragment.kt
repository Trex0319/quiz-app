package com.example.mob.ui.student

import android.app.AlertDialog
import android.os.CountDownTimer
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mob.data.model.Question
import com.example.mob.ui.adapter.AnswerQuestionAdapter
import com.example.mob.ui.base.BaseFragment
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentStudentQuizBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@AndroidEntryPoint
class StudentQuizFragment : BaseFragment<FragmentStudentQuizBinding>() {
    private lateinit var adapter: AnswerQuestionAdapter
    override val viewModel: StudentQuizViewModel by viewModels()
    private var countDownTimer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 0
    private var currentQuestionIndex = 0
    private var questions: List<Question> = emptyList()
    override fun getLayoutResource() = R.layout.fragment_student_quiz
    private val args: StudentQuizFragmentArgs by navArgs()
    private val quizId by lazy { args.quizId }

    override fun onBindView(view: View) {
        super.onBindView(view)

        setupAdapter()
        setupButton()
        lifecycleScope.launch {
            val quiz = viewModel.getQuizById(quizId)
            questions = quiz?.questions ?: emptyList()
            if (questions.isNotEmpty()) {
                displayQuestion(currentQuestionIndex)
            } else {
                Snackbar.make(view, "Quiz not found", Snackbar.LENGTH_LONG).show()
            }
        }

        lifecycleScope.launch {
            viewModel.finish.collect {
                Snackbar.make(view, "Thanks for attend this Quiz", Snackbar.LENGTH_SHORT).setBackgroundTint(
                    ContextCompat.getColor(requireContext(), R.color.green)
                ).show()
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                if (isLoading) {
                    binding?.loadingOverlay?.isVisible = true
                    loading()
                } else {
                    binding?.loadingOverlay?.isVisible = false
                }
            }
        }
    }

    private fun setupButton() {
        binding?.btnNext?.setOnClickListener {
            goToNextQuestion()
        }

        binding?.btnSubmit?.setOnClickListener {
            submitQuiz()
        }
    }

    private fun setupAdapter() {
        adapter = AnswerQuestionAdapter(emptyList())
        binding?.rvQuestions?.adapter = adapter
        binding?.rvQuestions?.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun displayQuestion(index: Int) {
        if (index < questions.size) {
            adapter.setQuestions(listOf(questions[index]))
            quizTimer(questions[index].timeLimit * 1000L)
            buttonVisibility(index)
        }
    }

    private fun goToNextQuestion() {
        currentQuestionIndex++
        if (currentQuestionIndex < questions.size) {
            displayQuestion(currentQuestionIndex)
        }
    }

    private fun buttonVisibility(index: Int) {
        binding?.btnNext?.visibility = if (index < questions.size - 1) View.VISIBLE else View.GONE
        binding?.btnSubmit?.visibility = if (index == questions.size - 1) View.VISIBLE else View.GONE
    }

    private fun submitQuiz() {
        lifecycleScope.launch {
            val selectedAnswers = adapter.getSelectedAnswers()
            val quiz = viewModel.getQuizById(quizId)
            if (quiz != null) {
                val totalScore = viewModel.calculateScore(quiz, selectedAnswers)
                val studentId = viewModel.getCurrentUserId()
                viewModel.saveResult(quizId, studentId, totalScore)
                showScoreDialog(totalScore)
            }
        }
    }

    private fun quizTimer(timeInMillis: Long) {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(timeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                timerView()
            }
            override fun onFinish() {
                goToNextQuestion()
            }
        }.start()
    }

    private fun timerView() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        binding?.tvTimeCount?.text = String.format("%02d:%02d", minutes, seconds)
    }

    private fun showScoreDialog(score: Int) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Quiz Completed")
            .setMessage("Your total score is $score")
            .setPositiveButton("OK") { _, _ ->
                view?.let { findNavController(it).popBackStack() }
            }
            .create()
        dialog.show()
        dialog.window?.decorView?.postDelayed({
            if (dialog.isShowing) {
                dialog.dismiss()
                view?.let { findNavController(it).popBackStack() }
            }
        }, 3000)
    }


    private fun loading() {
        binding?.loadingOverlay?.isVisible = true
        val tvLoadingText = binding?.tvLoadingText

        lifecycleScope.launch {
            var progress = 0
            while (progress < 100) {
                val randomIncrement = Random.nextInt(1, 15)
                progress += randomIncrement

                if (progress > 100) {
                    progress = 100
                }

                tvLoadingText?.text = getString(R.string.submit, progress)
                delay(Random.nextLong(50, 250))
            }
            binding?.loadingOverlay?.isVisible = false
        }
    }
}