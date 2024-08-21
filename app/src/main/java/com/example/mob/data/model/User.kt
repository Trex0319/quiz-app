package com.example.mob.data.model

import com.example.mob.core.utils.UserRole

data class User(
    val name: String,
    val email: String,
    val role: UserRole = UserRole.Student
) {
    companion object {
        fun fromMap(map: Map<*,*>): User {
            return User(
                name = map["Name"].toString(),
                email = map["email"].toString(),
                role =  UserRole.valueOf(map["role"] as? String ?: UserRole.Student.toString()),
                )
        }
    }
}