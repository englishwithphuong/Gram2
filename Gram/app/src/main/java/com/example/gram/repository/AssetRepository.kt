package com.example.gram.repository

import android.content.Context
import com.example.gram.model.SourceItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
            return@withContext lessonFiles.sorted().map { it.substringBeforeLast(".") }
        }
        emptyList()
    } catch (_: Exception) {
        emptyList()
    }
}

suspend fun getLessonContent(context: Context, sourceIndex: Int, lessonName: String): String = withContext(Dispatchers.IO) {
    val assetManager = context.assets
    try {
        val rawFolders = getSortedAssetFolders(context)
        if (sourceIndex in rawFolders.indices) {
            val folderName = rawFolders[sourceIndex]
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
