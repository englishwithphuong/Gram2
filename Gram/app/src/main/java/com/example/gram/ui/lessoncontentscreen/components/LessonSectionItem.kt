package com.example.gram.ui.lessoncontentscreen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gram.model.Section
import com.example.gram.ui.theme.KaitiFontFamily
import com.example.gram.ui.theme.Level1Color
import com.example.gram.ui.theme.Level2Color
import com.example.gram.ui.theme.Typography

@Composable
fun LessonSectionItem(section: Section) {
    when (section.type) {
        "text" -> LessonTextSection(section = section)
        "comment" -> LessonCommentSection(section = section)
    }
}

@Composable
fun LessonTextSection(section: Section) {
    val isLevel1 = section.level == 1
    val textSize = if (isLevel1) Typography.titleMedium.fontSize else Typography.bodyMedium.fontSize
    val textColor = if (isLevel1) Level1Color else Level2Color
    val fontWeight = if (isLevel1) Typography.titleMedium.fontWeight else Typography.bodyMedium.fontWeight
    val lineHeight = if(isLevel1) Typography.titleMedium.lineHeight else Typography.bodyMedium.lineHeight

    val annotatedContent = remember(section.content) {
        buildStyledChineseText(
            text = section.content,
            chineseFontFamily = KaitiFontFamily
        )
    }

    Text(
        text = annotatedContent,
        fontSize = textSize,
        color = textColor,
        fontWeight = fontWeight,
        lineHeight = lineHeight
    )
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun LessonCommentSection(section: Section) {
    val commentLines = remember(section.content) {
        section.content.split("\n")
    }

    Column {
        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Chú thích:",
            fontSize = Typography.bodyMedium.fontSize,
            color = Level1Color,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic
        )

        Spacer(modifier = Modifier.height(4.dp))

        commentLines.forEachIndexed { index, line ->
            if (line.isNotBlank()) {
                // Apply KaitiFontFamily to Chinese characters in the comment text
                val annotatedCommentLine = remember(line) {
                    buildStyledChineseText(
                        text = line,
                        chineseFontFamily = KaitiFontFamily
                    )
                }

                Text(
                    text = annotatedCommentLine,
                    fontSize = Typography.bodyMedium.fontSize,
                    color = Level2Color.copy(alpha = 0.85f),
                    fontStyle = FontStyle.Italic,
                    fontWeight = Typography.bodySmall.fontWeight,
                    lineHeight = 20.sp
                )
                if (index < commentLines.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}

/**
 * Scans the string and applies a custom font family only to Chinese characters.
 */
fun buildStyledChineseText(text: String, chineseFontFamily: FontFamily): AnnotatedString {
    return buildAnnotatedString {
        var i = 0
        while (i < text.length) {
            val char = text[i]
            if (isChineseCharacter(char)) {
                withStyle(SpanStyle(fontFamily = chineseFontFamily)) {
                    append(char)
                }
            } else {
                append(char)
            }
            i++
        }
    }
}

/**
 * Checks if a character falls within the CJK Unified Ideographs Unicode ranges.
 */
fun isChineseCharacter(c: Char): Boolean {
    val block = Character.UnicodeBlock.of(c)
    return block === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS ||
            block === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A ||
            block === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B ||
            block === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
}
