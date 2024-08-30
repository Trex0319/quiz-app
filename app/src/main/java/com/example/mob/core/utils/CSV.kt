package com.example.mob.core.utils

import android.content.Context
import android.net.Uri
import com.example.mob.data.model.Question
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

object CSV {
    fun readCSVFile(context: Context, uri: Uri): List<Question> {
        val questions = mutableListOf<Question>()
        try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8)).use { reader ->
                    reader.readLine()
                    reader.forEachLine { line ->
                        val content = line.split(",")
                        if (content.size >= 9) {
                            val question = Question(
                                questionId = content[0],
                                questionText = content[1],
                                options = content.subList(2, 6),
                                correctAnswer = content[6],
                                timeLimit = content[7].toIntOrNull() ?: 0,
                                mark = content[8].toIntOrNull() ?: 1
                            )
                            questions.add(question)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return questions
    }
}