package com.example.mob.data.model

import java.util.Date

data class Quiz(
    val quizId: String,
    val title: String,
    val questions: List<Question> = emptyList(),
    val totalScore: Int = 0,
    val createdBy: String,
    val createdAt: Long = System.currentTimeMillis(),
    val publishDate: Date? = null,
    val accessId: String = generateAccessId(),
    val status: Boolean = false
) {
    fun toMap(): Map<String, Any?> {
        return hashMapOf(
            "quizId" to quizId,
            "title" to title,
            "questions" to questions.map { it.toMap() },
            "totalScore" to totalScore,
            "createdBy" to createdBy,
            "createdAt" to createdAt,
            "publishDate" to publishDate?.time,
            "accessId" to accessId,
            "status" to status,
            )
    }



    companion object {
        fun generateAccessId(): String {
            return (100000..999999).random().toString()
        }
        fun fromMap(map: Map<String, Any?>): Quiz {
            val questionsList = (map["questions"] as? List<Map<String, Any?>>)?.map { Question.fromMap(it) } ?: emptyList()
            return Quiz(
                quizId = map["quizId"] as? String ?: "",
                title = map["title"] as? String ?: "",
                questions = questionsList,
                totalScore = questionsList.sumOf { it.mark ?: 0 },
                createdBy = map["createdBy"] as? String ?: "",
                createdAt = map["createdAt"] as? Long ?: System.currentTimeMillis(),
                publishDate = map["publishDate"]?.let { Date(it as Long) },
                accessId = map["accessId"] as? String ?: "",
                status = map["status"] as? Boolean ?: false

            )
        }
    }
}