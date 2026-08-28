package com.example.gram.model

import kotlinx.serialization.Serializable

@Serializable
data class Lesson(
    val lessonId: String,
    val title: String,
    val sections: List<Section>
)

@Serializable
data class Section(
    val type: String,
    val level: Int = 1,
    val content: String
)

data class LessonItem(
    val index: Int,
    val fileName: String,
    val title: String
)
