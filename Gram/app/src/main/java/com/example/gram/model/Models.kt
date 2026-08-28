package com.example.gram.model

import kotlinx.serialization.Serializable

@Serializable
data class SourceItem(
    val index: Int,
    val title: String
)
