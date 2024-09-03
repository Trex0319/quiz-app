package com.example.mob.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mob.data.model.StudentQuizCompletion
import com.example.quizapp.databinding.LayoutStudentRankingBinding

class StudentQuizAdapter: RecyclerView.Adapter<StudentQuizAdapter.RankingViewHolder>() {
    private var students: List<StudentQuizCompletion> = emptyList()

    fun submitList(list: List<StudentQuizCompletion>) {
        students = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutStudentRankingBinding.inflate(inflater, parent, false)
        return RankingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        val item = students[position]
        holder.bind(item, position +1)
    }

    override fun getItemCount(): Int = students.size

    class RankingViewHolder(
        private val binding: LayoutStudentRankingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(student: StudentQuizCompletion, rank: Int) {
            binding.run {
                Glide.with(binding.root)
            }
            binding.tvRanking.text = "${rank}. "
            binding.tvStudentName.text = "${student.name}"
            binding.tvStudentScore.text = student.totalScore.toString()

        }
    }
}