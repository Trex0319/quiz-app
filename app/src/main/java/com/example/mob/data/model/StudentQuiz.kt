package com.example.mob.data.model

data class StudentQuiz(
    var studentId: String,
    val firstName: String,
    val lastName: String,
    val profilePicture: String? = null,
    val totalScore: Int = 0,
    val completionId: String? = null,
) {
    fun toMap(): Map<String, Any?> {
        return hashMapOf(
            "studentId" to studentId,
            "totalScore" to totalScore,
            "firstName" to firstName,
            "lastName" to lastName,
            "profilePicture" to profilePicture
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any?>, id: String? = null): StudentQuiz{
            return StudentQuiz(
                studentId = map["studentId"] as? String ?: "",
                firstName = map["firstName"] as? String ?: "",
                lastName = map["lastName"] as? String ?: "",
                profilePicture = map["profilePicture"] as? String,
                totalScore = map["totalScore"] as? Int ?: 0,
                completionId = id,
            )
        }
    }
}