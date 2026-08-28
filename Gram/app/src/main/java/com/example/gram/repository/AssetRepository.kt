package com.example.gram.repository

import android.content.Context
import com.example.gram.model.Lesson
import com.example.gram.model.SourceItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader

suspend fun getSources(context: Context): List<SourceItem> = withContext(Dispatchers.IO) {
    try {
        val rawFolders = getSortedAssetFolders(context)
        rawFolders.mapIndexed { index, folderName ->
            SourceItem(
                index = index,
                title = formatSourceName(folderName)
            )
        }
    } catch (_: Exception) {
        emptyList()
    }
}

suspend fun getLessonsForSource(context: Context, sourceIndex: Int): List<String> = withContext(Dispatchers.IO) {
    val assetManager = context.assets
    try {
        val rawFolders = getSortedAssetFolders(context)
        if (sourceIndex in rawFolders.indices) {
            val folderName = rawFolders[sourceIndex]
            val lessonFiles = assetManager.list(folderName) ?: emptyArray()

            // Apply natural sorting here
            return@withContext lessonFiles
                .sortedWith(naturalOrderComparator)
                .map { it.substringBeforeLast(".") }
        }
        emptyList()
    } catch (_: Exception) {
        emptyList()
    }
}

private val naturalOrderComparator = Comparator<String> { s1, s2 ->
    val regex = Regex("\\d+")
    val match1 = regex.find(s1)?.value?.toIntOrNull()
    val match2 = regex.find(s2)?.value?.toIntOrNull()

    if (match1 != null && match2 != null) {
        // If both strings contain numbers, compare them numerically
        val comp = match1.compareTo(match2)
        if (comp != 0) return@Comparator comp
    }
    // Fallback to standard alphabetical comparison if no numbers or numbers are equal
    s1.compareTo(s2)
}

suspend fun getLessonContent(context: Context, sourceIndex: Int, lessonName: String): Lesson? = withContext(Dispatchers.IO) {
    val assetManager = context.assets
    try {
        val rawFolders = getSortedAssetFolders(context)
        if (sourceIndex in rawFolders.indices) {
            val folderName = rawFolders[sourceIndex]
            val files = assetManager.list(folderName) ?: emptyArray()

            val targetFile =
                files.find { it.substringBeforeLast(".") == lessonName } ?: "$lessonName.json"

            val path = "$folderName/$targetFile"
            val inputStream = assetManager.open(path)
            val jsonString = BufferedReader(InputStreamReader(inputStream)).use { it.readText() }

            return@withContext Json.decodeFromString<Lesson>(jsonString)
        }
        null
    } catch (_: Exception) {
        null
    }
}

// --- Helper Functions to Avoid Code Duplication ---

private fun getSortedAssetFolders(context: Context): List<String> {
    val assetManager = context.assets
    val regex = Regex("^\\d{2}_.*")
    return assetManager.list("")
        ?.filter { it.matches(regex) }
        ?.sorted()
        ?: emptyList()
}

private fun formatSourceName(folderName: String): String {
    val nameMap = mapOf(
        "01_301_cau_dam_thoai_tieng_hoa" to "301 Câu Đàm Thoại Tiếng Hoa",
        "02_ngu_phap_tieng_trung_co_ban" to "Ngữ Pháp Tiếng Trung Cơ Bản"
    )
    return nameMap[folderName] ?: folderName
}
