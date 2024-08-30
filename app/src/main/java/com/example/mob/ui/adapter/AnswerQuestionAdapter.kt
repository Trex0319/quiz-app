package com.example.mob.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mob.data.model.Question
import com.example.quizapp.databinding.LayoutAnswerCardBinding

class AnswerQuestionAdapter(
    private var questions: List<Question>
) : RecyclerView.Adapter<AnswerQuestionAdapter.AnswerQuestionViewHolder>() {
    private val selectedAnswers = mutableMapOf<String, String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerQuestionViewHolder {
        val binding = LayoutAnswerCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AnswerQuestionViewHolder(binding)
    }

    override fun getItemCount(): Int = questions.size

    override fun onBindViewHolder(holder: AnswerQuestionViewHolder, position: Int) {
        val item = questions[position]
        holder.bind(item)
    }

    fun setQuestions(questions: List<Question>) {
        this.questions = questions
        clearSelectedAnswer()
        notifyDataSetChanged()
    }

    private fun clearSelectedAnswer() {
        selectedAnswers.clear()
    }

    fun getSelectedAnswers(): Map<String, String> {
        return selectedAnswers
    }

    inner class AnswerQuestionViewHolder(
        private val binding: LayoutAnswerCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(question: Question) {
            binding.tvQuestionText.text = question.questionText
            binding.tvMark.text = "Mark: ${question.mark} "
            binding.optionA.text = "A. ${question.options[0]}"
            binding.optionB.text = "B. ${question.options[1]}"
            binding.optionC.text = "C. ${question.options[2]}"
            binding.optionD.text = "D. ${question.options[3]}"

            binding.optionA.isChecked = false
            binding.optionB.isChecked = false
            binding.optionC.isChecked = false
            binding.optionD.isChecked = false

            binding.optionA.setOnClickListener { selectedAnswers[question.questionId] = question.options[0] }
            binding.optionB.setOnClickListener { selectedAnswers[question.questionId] = question.options[1] }
            binding.optionC.setOnClickListener { selectedAnswers[question.questionId] = question.options[2] }
            binding.optionD.setOnClickListener { selectedAnswers[question.questionId] = question.options[3] }
        }
    }
}