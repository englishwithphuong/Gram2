package com.example.gram.ui.sourcesscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gram.model.SourceItem
import com.example.gram.ui.theme.TitleColor

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

@Composable
fun SourceItemRow(sourceItem: SourceItem, onClick: () -> Unit) {
    Text(
        text = sourceItem.title,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = TitleColor,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 4.dp)
    )
}
