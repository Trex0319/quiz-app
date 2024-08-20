package com.example.mob.data.repo

import com.example.mob.data.model.Task
import kotlinx.coroutines.flow.Flow

interface TodoRepo {

    fun getAllTasks(): Flow<List<Task>>

    suspend fun addTask(task: Task) : String?

    suspend fun deleteTask(id: String)

    suspend fun updateTask(task: Task)

    suspend fun getTaskById(id: String): Task?
}