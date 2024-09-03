package com.example.mob.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mob.data.model.Question
import com.example.quizapp.databinding.LayoutQuestionCardBinding

class QuestionAdapter(
    private var questions: List<Question>
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = LayoutQuestionCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuestionViewHolder(binding)
    }
    override fun getItemCount(): Int = questions.size
    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val item = questions[position]
        holder.bind(item)
    }
    fun setQuestions(questions: List<Question>) {
        this.questions = questions
        notifyDataSetChanged()
    }
    inner class QuestionViewHolder(
        private val binding: LayoutQuestionCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(question: Question) {
            binding.tvQuestionText.text = question.questionText
            binding.tvOptionA.text = "A. ${question.options[0]}"
            binding.tvOptionB.text = "B. ${question.options[1]}"
            binding.tvOptionC.text = "C. ${question.options[2]}"
            binding.tvOptionD.text = "D. ${question.options[3]}"
        }
    }
}