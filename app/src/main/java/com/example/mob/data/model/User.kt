package com.example.mob.data.model

data class User(
    val name: String,
    val email: String,
    val profilePic: String? = null
) {
    companion object {
        fun fromMap(map: Map<*,*>): User {
            return User(
                name = map["Name"].toString(),
                email = map["email"].toString(),
                profilePic = map["profilePic"].toString()
            )
        }
    }
}