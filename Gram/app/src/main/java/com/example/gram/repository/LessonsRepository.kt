package com.example.gram.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun getLessonsForSource(context: Context, sourceIndex: Int): List<String> = withContext(Dispatchers.IO) {
    val assetManager = context.assets
    try {
        val regex = Regex("^\\d{2}_.*")
        val rawFolders = assetManager.list("")
            ?.filter { it.matches(regex) }
            ?.sorted()
            ?: emptyList()

        if (sourceIndex in rawFolders.indices) {
            val folderName = rawFolders[sourceIndex]
            // List contents inside the specific source folder (e.g., "01_301_cau_dam_thoai_tieng_hoa")
            val lessonFiles = assetManager.list(folderName) ?: emptyArray()

            // Format or filter the lesson files/folders as needed
            return@withContext lessonFiles.sorted().map { formatLessonName(it) }
        }
        emptyList()
    } catch (_: Exception) {
        emptyList()
    }
}

private fun formatLessonName(fileName: String): String {
    // Optional: Clean up file extensions or prefixes if needed
    return fileName.substringBeforeLast(".")
}
