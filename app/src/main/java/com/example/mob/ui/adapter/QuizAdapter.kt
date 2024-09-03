package com.example.mob.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mob.data.model.Quiz
import com.example.quizapp.R
import com.example.quizapp.databinding.LayoutQuizCardBinding
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class QuizAdapter : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    private var quizzes: List<Quiz> = emptyList()
    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val binding = LayoutQuizCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuizViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return quizzes.size
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val item = quizzes[position]
        holder.bind(item)
    }

    fun setQuizzes(quizzes: List<Quiz>) {
        this.quizzes = quizzes
        notifyDataSetChanged()
    }

    inner class QuizViewHolder(
        private val binding: LayoutQuizCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        fun bind(quiz: Quiz) {
            binding.tvQuizTitle.text = quiz.title

            val publishDate = quiz.publishDate?.let { dateFormatter.format(it) } ?: ""

            binding.tvPublishDate.text = publishDate

            val currentDate = Date()
            if (quiz.publishDate != null && quiz.publishDate.after(currentDate)) {
                binding.root.setBackgroundColor(R.color.red.toInt())
            } else {
                binding.root.setBackgroundColor(Color.TRANSPARENT)
            }

            binding.cvQuiz.setOnClickListener {
                listener?.onClick(quiz)
            }

            binding.ivEdit.setOnClickListener {
                listener?.onClickEdit(quiz)
            }

            binding.ivDelete.setOnClickListener {
                listener?.onClickDelete(quiz)
            }

            binding.tvAccessId.text = quiz.accessId
            binding.btnCopyAccessId.setOnClickListener {
                val clipboard = ContextCompat.getSystemService(
                    binding.root.context,
                    android.content.ClipboardManager::class.java
                )
                val clip = android.content.ClipData.newPlainText("Access ID", quiz.accessId)
                clipboard?.setPrimaryClip(clip)
                Snackbar.make(binding.root, "Access ID copied to clipboard", Snackbar.LENGTH_SHORT).show()
            }

        }
    }

    interface Listener {
        fun onClick(quiz: Quiz)
        fun onClickEdit(quiz: Quiz)
        fun onClickDelete(quiz: Quiz)
    }
}