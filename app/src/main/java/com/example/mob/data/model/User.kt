package com.example.mob.data.model

data class User(
    val name: String,
    val email: String
) {
    companion object {
        fun fromMap(map: Map<*,*>): User {
            return User(
                name = map["Name"].toString(),
                email = map["email"].toString()

            )
        }
    }
}