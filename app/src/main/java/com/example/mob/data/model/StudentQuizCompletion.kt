package com.example.mob.data.model

data class StudentQuizCompletion(
    var studentId: String,
    val name: String,
    val profilePicture: String? = null,
    val totalScore: Int = 0,
    val completionId: String? = null,
) {
    fun toMap(): Map<String, Any?> {
        return hashMapOf(
            "studentId" to studentId,
            "totalScore" to totalScore,
            "name" to name,
            "profilePicture" to profilePicture
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any?>, id: String? = null): StudentQuizCompletion {
            return StudentQuizCompletion(
                studentId = map["studentId"] as? String ?: "",
                name = map["name"] as? String ?: "",
                profilePicture = map["profilePicture"] as? String,
                totalScore = map["totalScore"] as? Int ?: 0,
                completionId = id,
            )
        }
    }
}