package com.example.mob.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mob.data.model.StudentQuiz
import com.example.quizapp.R
import com.example.quizapp.databinding.LayoutStudentRankingBinding

class StudentQuizAdapter: RecyclerView.Adapter<StudentQuizAdapter.RankingViewHolder>() {
    private var students: List<StudentQuiz> = emptyList()

    fun submitList(list: List<StudentQuiz>) {
        students = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutStudentRankingBinding.inflate(inflater, parent, false)
        return RankingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        val student = students[position]
        holder.bind(student, position + 4)
    }

    override fun getItemCount(): Int = students.size

    class RankingViewHolder(
        private val binding: LayoutStudentRankingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(student: StudentQuiz, rank: Int) {
            binding.run {
                Glide.with(binding.root)
                    .load(student.profilePicture)
                    .placeholder(R.drawable.ic_person)
                    .into(binding.ivStudentImage)
            }
            binding.tvStudentName.text = "${student.firstName} ${student.lastName}"
            binding.tvStudentScore.text = student.totalScore.toString()

        }
    }
}