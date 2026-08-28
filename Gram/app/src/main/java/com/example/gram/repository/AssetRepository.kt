package com.example.gram.repository

import android.content.Context
import com.example.gram.model.Lesson
import com.example.gram.model.LessonItem
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

suspend fun getLessonsForSource(context: Context, sourceIndex: Int): List<LessonItem> =
    withContext(Dispatchers.IO) {
        val assetManager = context.assets
        try {
            val rawFolders = getSortedAssetFolders(context)
            if (sourceIndex in rawFolders.indices) {
                val sourceFolder = rawFolders[sourceIndex]

                // 1. Get group folders (e.g., "1", "11", "21") and sort them numerically
                val groupFolders = assetManager.list(sourceFolder) ?: emptyArray()
                val sortedGroups = groupFolders.sortedWith(naturalOrderComparator)

                // 2. Gather all json files from all group folders in order
                val allSortedFiles = mutableListOf<String>()
                for (group in sortedGroups) {
                    val groupPath = "$sourceFolder/$group"
                    val files = assetManager.list(groupPath) ?: emptyArray()
                    val sortedFiles = files.sortedWith(naturalOrderComparator)
                    for (file in sortedFiles) {
                        allSortedFiles.add("$group/$file")
                    }
                }

                return@withContext allSortedFiles.mapIndexed { index, relativePath ->
                    val path = "$sourceFolder/$relativePath"
                    val fileName = relativePath.substringAfterLast("/")
                    val title = try {
                        val inputStream = assetManager.open(path)
                        val jsonString =
                            BufferedReader(InputStreamReader(inputStream)).use { it.readText() }
                        Json.decodeFromString<Lesson>(jsonString).title
                    } catch (_: Exception) {
                        fileName.substringBeforeLast(".") // Fallback if parsing fails
                    }

                    LessonItem(
                        index = index,
                        fileName = fileName.substringBeforeLast("."),
                        title = title
                    )
                }
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
        val comp = match1.compareTo(match2)
        if (comp != 0) return@Comparator comp
    }
    s1.compareTo(s2)
}

suspend fun getLessonContent(context: Context, sourceIndex: Int, lessonIndex: Int): Lesson? =
    withContext(Dispatchers.IO) {
        val assetManager = context.assets
        try {
            val rawFolders = getSortedAssetFolders(context)
            if (sourceIndex in rawFolders.indices) {
                val sourceFolder = rawFolders[sourceIndex]

                // Reconstruct the exact same flattened file list order
                val groupFolders = assetManager.list(sourceFolder) ?: emptyArray()
                val sortedGroups = groupFolders.sortedWith(naturalOrderComparator)

                val allSortedFiles = mutableListOf<String>()
                for (group in sortedGroups) {
                    val groupPath = "$sourceFolder/$group"
                    val files = assetManager.list(groupPath) ?: emptyArray()
                    val sortedFiles = files.sortedWith(naturalOrderComparator)
                    for (file in sortedFiles) {
                        allSortedFiles.add("$group/$file")
                    }
                }

                if (lessonIndex in allSortedFiles.indices) {
                    val targetRelativePath = allSortedFiles[lessonIndex]
                    val path = "$sourceFolder/$targetRelativePath"
                    val inputStream = assetManager.open(path)
                    val jsonString =
                        BufferedReader(InputStreamReader(inputStream)).use { it.readText() }

                    return@withContext Json.decodeFromString<Lesson>(jsonString)
                }
            }
            null
        } catch (_: Exception) {
            null
        }
    }

// --- Helper Functions to Avoid Code Duplication ---

private fun getSortedAssetFolders(context: Context): List<String> {
    val assetManager = context.assets
    val regex = Regex("^(\\d+)_.*")

    return assetManager.list("")
        ?.filter { folderName ->
            regex.matches(folderName)
        }
        ?.sortedWith { a, b ->
            val numberA = regex.find(a)?.groupValues?.get(1)?.toIntOrNull() ?: Int.MAX_VALUE
            val numberB = regex.find(b)?.groupValues?.get(1)?.toIntOrNull() ?: Int.MAX_VALUE

            numberA.compareTo(numberB)
        }
        ?: emptyList()
}

private fun formatSourceName(folderName: String): String {
    val nameMap = mapOf(
        "1_301_cau_dam_thoai_tieng_hoa" to "301 Câu Đàm Thoại Tiếng Hoa",
        "2_100_cau_truc_ngu_phap_tieng_trung_thong_dung" to "100 Cấu Trúc Ngữ Pháp Tiếng Trung Thông Dụng",
        "3_giao_trinh_han_ngu_boya" to "Giáo Trình Hán Ngữ Boya"
    )
    return nameMap[folderName] ?: folderName
}
