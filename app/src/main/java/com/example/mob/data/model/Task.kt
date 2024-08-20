package com.example.mob.data.model

data class Task(
    val id: String?= null,
    val title: String = "",
    val desc: String = "",
    val priority: Int = 0,
//    val extra: Extra? = null
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "title" to title,
            "desc" to desc,
            "priority" to priority,
//            "extra" to extra
        )
    }

    companion object  {
        fun fromMap(map: Map<String, Any>): Task {
            return Task (
                title = map["title"].toString(),
                desc = map["desc"]. toString(),
                priority = map["priority"].toString().toIntOrNull() ?: 0,
//                extra = Extra.fromMap(map["extra"] as Map<*, *>?)
            )
        }
    }
}
