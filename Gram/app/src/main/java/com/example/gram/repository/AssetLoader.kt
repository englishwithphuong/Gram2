package com.example.gram.repository

import android.content.Context
import com.example.gram.model.SourceItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun getSources(context: Context): List<SourceItem> = withContext(Dispatchers.IO) {
    val assetManager = context.assets
    try {
        val regex = Regex("^\\d{2}_.*")

        val rawFolders = assetManager.list("")
            ?.filter { it.matches(regex) }
            ?.sorted()
            ?: emptyList()

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

private fun formatSourceName(folderName: String): String {
    val nameMap = mapOf(
        "01_301_cau_dam_thoai_tieng_hoa" to "301 Câu Đàm Thoại Tiếng Hoa",
        "02_ngu_phap_tieng_trung_co_ban" to "Ngữ Pháp Tiếng Trung Cơ Bản"
    )
    return nameMap[folderName] ?: folderName
}
