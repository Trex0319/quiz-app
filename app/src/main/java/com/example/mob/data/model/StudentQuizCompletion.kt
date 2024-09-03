package com.example.mob.data.model

data class StudentQuizCompletion(
    var studentId: String,
    val name: String,
    val totalScore: Int = 0,
    val completionId: String? = null,
) {
    fun toMap(): Map<String, Any?> {
        return hashMapOf(
            "studentId" to studentId,
            "totalScore" to totalScore,
            "name" to name,
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any?>, id: String? = null): StudentQuizCompletion {
            return StudentQuizCompletion(
                studentId = map["studentId"] as? String ?: "",
                name = map["name"] as? String ?: "",
                totalScore = (map["totalScore"] as? Long)?.toInt() ?: 0,
                completionId = id,
            )
        }
    }
}