package com.example.gram.ui.sourcesscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gram.model.SourceItem

@Composable
fun SourcesScreen(
    sources: List<SourceItem>,
    onSourceClick: (SourceItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 32.dp, horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(sources) { sourceItem ->
            SourceItemRow(sourceItem = sourceItem) {
                onSourceClick(sourceItem)
            }
        }
    }
}
