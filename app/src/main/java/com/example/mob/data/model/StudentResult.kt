package com.example.mob.data.model

data class StudentResult(
    val resultId: String = "",
    val studentId: String = "",
    val quizId: String = "",
    val score: Int = 0,
    val submittedAt: Long = System.currentTimeMillis()
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "resultId" to resultId,
            "studentId" to studentId,
            "quizId" to quizId,
            "score" to score,
            "submittedAt" to submittedAt

        )
    }

    companion object {
        fun fromMap(map: Map<String, Any>): StudentResult {
            return StudentResult(
                resultId = map["resultId"] as String,
                studentId = map["studentId"] as String,
                quizId = map["quizId"] as String,
                score = (map["score"] as Long).toInt(),
                submittedAt = map["submittedAt"] as Long
            )
        }
    }
}