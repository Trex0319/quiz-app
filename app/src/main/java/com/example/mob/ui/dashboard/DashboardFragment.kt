package com.example.mob.ui.dashboard

import android.app.AlertDialog
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mob.data.model.Quiz
import com.example.mob.ui.adapter.QuizAdapter
import com.example.mob.ui.base.BaseFragment
import com.example.mob.ui.home.HomeViewModel
import com.example.quizapp.R
import com.example.quizapp.databinding.AlertDeleteQuizBinding
import com.example.quizapp.databinding.FragmentDashboardBinding
import com.example.quizapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardFragment : BaseFragment<FragmentDashboardBinding>() {
    override val viewModel: DashboardViewModel by viewModels()

    private lateinit var quizAdapter: QuizAdapter

    override fun getLayoutResource() = R.layout.fragment_dashboard


    override fun onBindData(view: View) {
        super.onBindData(view)
        setupRecyclerView()

        viewModel.quiz.observe(viewLifecycleOwner) { quizzes ->
            quizAdapter.setQuizzes(quizzes)
        }

        lifecycleScope.launch {
            viewModel.quizzes.collect { quiz ->
                quizAdapter.setQuizzes(quiz)
                binding?.tvNoContent?.isVisible = quizAdapter.itemCount != 0
            }
        }
    }

    override fun onBindView(view: View) {
        super.onBindView(view)

        binding?.btnAddQuiz?.setOnClickListener {
            findNavController(this@DashboardFragment).navigate(
                DashboardFragmentDirections.actionDashboardFragmentToAddQuizFragment()
            )
        }
    }

    private fun setupRecyclerView() {
        quizAdapter = QuizAdapter()
        val layoutManager = LinearLayoutManager(requireContext())
        binding?.rvQuiz?.adapter = quizAdapter
        binding?.rvQuiz?.layoutManager = layoutManager

        quizAdapter.listener = object: QuizAdapter.Listener {
            override fun onClick(quiz: Quiz) {
                TODO()
            }

            override fun onClickEdit(quiz: Quiz) {
                quiz.quizId.let {
                    findNavController(this@DashboardFragment).navigate(
                        DashboardFragmentDirections.actionDashboardFragmentToEditQuizFragment(it)
                    )
                }
            }

            override fun onClickDelete(quiz: Quiz) {
                val alertView = AlertDeleteQuizBinding.inflate(layoutInflater)
                val deleteDialog = AlertDialog.Builder(requireContext())
                deleteDialog.setView(alertView.root)
                val temporaryDeleteDialog = deleteDialog.create()

                alertView.tvTitle.text = "Are you sure?"
                alertView.tvBody.text = "You want to delete this Quiz? \nAction cannot be undone."

                alertView.btnDelete.setOnClickListener {
                    viewModel.deleteQuiz(quiz.quizId)
                    temporaryDeleteDialog.dismiss()
                }
                alertView.btnCancel.setOnClickListener {
                    temporaryDeleteDialog.dismiss()
                }
                temporaryDeleteDialog.show()
            }
        }
    }
}
