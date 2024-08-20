package com.example.mob.data.repo

import com.example.mob.core.service.AuthService
import com.example.mob.data.model.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class TodoRepoFirebase(
    private val authService: AuthService
): TodoRepo {
    private fun getCollection(): CollectionReference {
        val uid = authService.getUid() ?: throw Exception("User doesn't exist")
        return Firebase.firestore
            .collection("root_db/$uid/todos")
    }

    suspend fun getTasksByTitle(title: String): List<Task> {
        val res = getCollection().orderBy("title")
            .startAt(title)
            .endAt("$title\uf8ff").get().await()

        val tasks = mutableListOf<Task>()
        res.documents.forEach{ doc ->
            doc.data?.let {
                val task = Task.fromMap(it)
                tasks.add(task)
            }
        }
        return tasks
    }

    override fun getAllTasks() = callbackFlow {
        val listener = getCollection().addSnapshotListener { value, error ->
            if(error !=null) {
                throw error
            }

            val tasks = mutableListOf<Task>()

            value?.documents?.map { item ->
                item.data?.let {taskMap ->
                    val task = Task.fromMap(taskMap)
                    tasks.add(task.copy(id = item.id))
                }
            }
            trySend(tasks)
        }
        awaitClose{
            listener.remove()
        }
    }
    override suspend fun addTask(task: Task): String? {
        val res = getCollection().add(task.toMap()).await()
        return res?.id
    }

    override suspend fun deleteTask(id: String) {
        getCollection().document(id).delete().await()
    }

    override suspend fun updateTask(task: Task) {
        getCollection().document(task.id!!).set(task.toMap()).await()
    }

    override suspend fun getTaskById(id: String): Task? {
        val res = getCollection().document(id).get().await()
        return res.data?.let { Task.fromMap(it).copy(id = res.id) }
    }

    suspend fun getGreetings(): List<String> {
        val greetCollection = Firebase.firestore.collection("greetings")
        val res = greetCollection.get().await()
        val msgs = mutableListOf<String>()


        res.documents.map { doc ->
            doc.data?.let {
                val msg = it["msg"].toString()
                msgs.add(msg)
            }
        }
        return msgs
    }
}