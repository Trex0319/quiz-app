package com.example.mob.data.model

data class Question(
    val questionId: String,
    val questionText: String,
    val options: List<String>,
    val correctAnswer: String,
    val timeLimit: Int = 30,
    val mark: Int = 1,
    val createdAt: Long = System.currentTimeMillis(),
) {
    fun toMap(): Map<String, Any?> {
        return hashMapOf(
            "questionId" to questionId,
            "questionText" to questionText,
            "options" to options,
            "correctAnswer" to correctAnswer,
            "timeLimit" to timeLimit,
            "mark" to mark,
            "createdAt" to createdAt,
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any?>): Question {
            return Question(
                questionId = map["questionId"] as? String ?: "",
                questionText = map["questionText"] as? String ?: "",
                options = (map["options"] as? List<String>) ?: emptyList(),
                correctAnswer = map["correctAnswer"] as? String ?: "",
                timeLimit = map["timeLimit"] as? Int ?: 30,
                mark = map["mark"] as? Int ?: 1,
                createdAt = map["createdAt"] as? Long ?: System.currentTimeMillis(),
            )
        }
    }
}