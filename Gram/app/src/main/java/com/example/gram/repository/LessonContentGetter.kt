package com.example.gram.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

suspend fun getLessonContent(context: Context, sourceIndex: Int, lessonName: String): String = withContext(Dispatchers.IO) {
    val assetManager = context.assets
    try {
        val regex = Regex("^\\d{2}_.*")
        val rawFolders = assetManager.list("")
            ?.filter { it.matches(regex) }
            ?.sorted()
            ?: emptyList()

        if (sourceIndex in rawFolders.indices) {
            val folderName = rawFolders[sourceIndex]

            // Find the actual file matching the lessonName (handling potential extensions)
            val files = assetManager.list(folderName) ?: emptyArray()
            val targetFile = files.find { it.substringBeforeLast(".") == lessonName } ?: "$lessonName.txt"

            val path = "$folderName/$targetFile"

            val inputStream = assetManager.open(path)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val content = reader.readText()
            reader.close()
            return@withContext content
        }
        "Source folder not found."
    } catch (e: Exception) {
        "Failed to load lesson content: ${e.localizedMessage}"
    }
}
