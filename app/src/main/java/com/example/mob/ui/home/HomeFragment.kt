package com.example.mob.ui.home

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mob.core.service.StorageService
import com.example.mob.data.model.StudentQuizCompletion
import com.example.mob.ui.adapter.StudentQuizAdapter
import com.example.mob.ui.base.BaseFragment
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    @Inject
    lateinit var storageService: StorageService
    override val viewModel: HomeViewModel by viewModels()
    private lateinit var studentQuizAdapter: StudentQuizAdapter
    override fun getLayoutResource() = R.layout.fragment_home

    override fun onBindView(view: View) {
        super.onBindView(view)
        studentQuizAdapter = StudentQuizAdapter()
        viewModel.loadCompletions()
        viewModel.completions.observe(viewLifecycleOwner) { completions ->
            if (completions.isNotEmpty()) {
                setupRankingViews(completions)
            } else {
                binding?.tvNoRanking?.visibility = View.VISIBLE
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

        binding?.btnAccessQuiz?.setOnClickListener {
            showAccessDialog()
        }
    }

    private fun showAccessDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.layout_entry_quiz_passcode, null)
        val etAccessId = dialogView.findViewById<EditText>(R.id.etAccessId)

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton(getString(R.string.access)) { _, _ ->
                val accessId = etAccessId.text.toString()
                verifyAccessDetails(accessId)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun verifyAccessDetails(accessId: String) {
        lifecycleScope.launch {
            val studentId = viewModel.getCurrentUserId()
            val quizId = viewModel.verifyQuizAccess(accessId)
            if (quizId != null) {
                val hasCompleted = viewModel.checkResult(quizId, studentId)
                if (!hasCompleted) {
                    val action = HomeFragmentDirections.actionHomeFragmentToStudentQuizFragment(quizId)
                    findNavController(this@HomeFragment).navigate(action)
                } else {
                    Snackbar.make(requireView(), getString(R.string.completed_quiz), Snackbar.LENGTH_LONG).show()
                }
            } else {
                Snackbar.make(requireView(), getString(R.string.invalid_details), Snackbar.LENGTH_LONG).show()
            }
        }
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

                tvLoadingText?.text = getString(R.string.verifying, progress)
                delay(Random.nextLong(50, 250))
            }
            binding?.loadingOverlay?.isVisible = false
        }
    }


    private fun setupRankingViews(completions: List<StudentQuizCompletion>) {
        val sortedCompletions = completions.sortedByDescending { it.totalScore }
        val topTen = sortedCompletions.take(10)

        if (topTen.isNotEmpty()) {
            binding?.rvRankings?.visibility = View.VISIBLE
            studentQuizAdapter.submitList(topTen)
            binding?.rvRankings?.adapter = studentQuizAdapter
            binding?.rvRankings?.layoutManager = LinearLayoutManager(requireContext())
            binding?.tvNoRanking?.visibility = View.GONE
        } else {
            binding?.rvRankings?.visibility = View.GONE
            binding?.tvNoRanking?.visibility = View.VISIBLE
        }
    }

}