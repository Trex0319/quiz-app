package com.example.mob.data.model

data class Question(
    val questionId: String,
    val questionText: String,
    val options: List<String>,
    val correctAnswer: String,
    val timeLimit: Int = 15,
    val mark: Int = 1,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedBy: String? = null,
    val updatedAt: Long? = null
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
            "updatedBy" to updatedBy,
            "updatedAt" to updatedAt
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any?>): Question {
            return Question(
                questionId = map["questionId"] as? String ?: "",
                questionText = map["questionText"] as? String ?: "",
                options = (map["options"] as? List<String>) ?: emptyList(),
                correctAnswer = map["correctAnswer"] as? String ?: "",
                timeLimit = map["timeLimit"] as? Int ?: 15,
                mark = map["mark"] as? Int ?: 1,
                createdAt = map["createdAt"] as? Long ?: System.currentTimeMillis(),
                updatedBy = map["updatedBy"] as? String,
                updatedAt = map["updatedAt"] as? Long
            )
        }
    }
}