package com.example.gram.ui.lessoncontentscreen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gram.model.Section
import com.example.gram.ui.navigation.Screen
import com.example.gram.ui.theme.PrimaryBracketTextColor
import com.example.gram.ui.theme.KaitiFontFamily
import com.example.gram.ui.theme.Level1Color
import com.example.gram.ui.theme.Level2Color
import com.example.gram.ui.theme.LinkTextColor
import com.example.gram.ui.theme.SecondaryBracketTextColor
import com.example.gram.ui.theme.Typography

@Composable
fun LessonSectionItem(
    section: Section,
    navController: NavController,
    sourceIndex: Int
) {
    when (section.type) {
        "text" -> LessonTextSection(
            section = section,
            navController = navController,
            sourceIndex = sourceIndex
        )

        "numbered" -> LessonNumberedSection(
            section = section,
            navController = navController,
            sourceIndex = sourceIndex
        )

        "comment" -> LessonCommentSection(
            section = section
        )
    }
}

@Composable
fun LessonTextSection(
    section: Section,
    navController: NavController,
    sourceIndex: Int
) {
    val content = section.content ?: return

    val isLevel1 = section.level == 1

    val textSize =
        if (isLevel1) {
            Typography.titleMedium.fontSize
        } else {
            Typography.bodyMedium.fontSize
        }

    val textColor =
        if (isLevel1) {
            Level1Color
        } else {
            Level2Color
        }

    val fontWeight =
        if (isLevel1) {
            Typography.titleMedium.fontWeight
        } else {
            Typography.bodyMedium.fontWeight
        }

    val lineHeight =
        if (isLevel1) {
            Typography.titleMedium.lineHeight
        } else {
            Typography.bodyMedium.lineHeight
        }

    val annotatedContent = remember(content, sourceIndex) {
        buildStyledChineseText(
            text = content,
            chineseFontFamily = KaitiFontFamily,
            onLessonLinkClick = { fileName ->

                val lessonIndex =
                    fileName.toIntOrNull()?.minus(1)

                if (lessonIndex != null) {
                    navController.navigate(
                        Screen.LessonContent.createRoute(
                            sourceIndex = sourceIndex,
                            lessonIndex = lessonIndex
                        )
                    )
                }
            }
        )
    }

    Text(
        text = annotatedContent,
        style = TextStyle(
            fontSize = textSize,
            color = textColor,
            fontWeight = fontWeight,
            lineHeight = lineHeight
        )
    )

    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun LessonNumberedSection(
    section: Section,
    navController: NavController,
    sourceIndex: Int
) {
    val items = section.items ?: emptyList()

    Column {
        Spacer(modifier = Modifier.height(4.dp))

        items.forEachIndexed { index, item ->

            val annotatedItem = remember(item, sourceIndex) {
                buildStyledChineseText(
                    text = item,
                    chineseFontFamily = KaitiFontFamily,
                    onLessonLinkClick = { fileName ->

                        val lessonIndex =
                            fileName.toIntOrNull()?.minus(1)

                        if (lessonIndex != null) {
                            navController.navigate(
                                Screen.LessonContent.createRoute(
                                    sourceIndex = sourceIndex,
                                    lessonIndex = lessonIndex
                                )
                            )
                        }
                    }
                )
            }

            Row {
                Text(
                    text = "${index + 1}.",
                    fontSize = Typography.bodyMedium.fontSize,
                    color = Level2Color,
                    fontWeight = Typography.bodyMedium.fontWeight,
                    lineHeight = Typography.bodyMedium.lineHeight,
                    modifier = Modifier
                        .width(24.dp)
                        .alignByBaseline()
                )

                Text(
                    text = annotatedItem,
                    fontSize = Typography.bodyMedium.fontSize,
                    color = Level2Color,
                    fontWeight = Typography.bodyMedium.fontWeight,
                    lineHeight = Typography.bodyMedium.lineHeight,
                    modifier = Modifier.alignByBaseline()
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
        }
    }

    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun LessonCommentSection(section: Section) {
    val content = section.content ?: return
    val commentLines = remember(content) {
        content.split("\n")
    }

    Column {
        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Chú thích:",
            fontSize = Typography.bodySmall.fontSize,
            color = Level1Color,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            lineHeight = Typography.bodySmall.lineHeight
        )

        Spacer(modifier = Modifier.height(12.dp))

        commentLines.forEachIndexed { index, line ->
            if (line.isNotBlank()) {
                val annotatedCommentLine = remember(line) {
                    buildStyledChineseText(
                        text = line,
                        chineseFontFamily = KaitiFontFamily
                    )
                }

                Text(
                    text = annotatedCommentLine,
                    fontSize = Typography.bodySmall.fontSize,
                    color = Level2Color.copy(alpha = 0.85f),
                    fontStyle = FontStyle.Italic,
                    fontWeight = Typography.bodySmall.fontWeight,
                    lineHeight = Typography.bodySmall.lineHeight
                )

                if (index < commentLines.size - 1) {
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(12.dp))
}

/**
 * Appends text with a highlight color while preserving
 * the font distinction between Chinese and Latin characters.
 *
 * Chinese characters -> chineseFontFamily
 * Latin/pinyin/etc. -> default font
 */
private fun AnnotatedString.Builder.appendHighlightedText(
    text: String,
    color: Color,
    chineseFontFamily: FontFamily
) {
    withStyle(
        SpanStyle(
            color = color
        )
    ) {
        for (char in text) {
            if (isChineseCharacter(char)) {
                withStyle(
                    SpanStyle(
                        fontFamily = chineseFontFamily
                    )
                ) {
                    append(char)
                }
            } else {
                append(char)
            }
        }
    }
}

fun buildStyledChineseText(
    text: String,
    chineseFontFamily: FontFamily,
    onLessonLinkClick: ((String) -> Unit)? = null
): AnnotatedString {
    return buildAnnotatedString {
        var i = 0

        while (i < text.length) {

            // Invisible indentation text: [[ ... ]]
            if (text.startsWith("[[", i)) {
                val closingInvisible = text.indexOf(
                    "]]",
                    startIndex = i + 2
                )

                if (closingInvisible != -1) {
                    val invisibleContent = text.substring(
                        startIndex = i + 2,
                        endIndex = closingInvisible
                    )

                    withStyle(
                        SpanStyle(
                            color = Color.Transparent
                        )
                    ) {
                        append(invisibleContent)
                    }

                    i = closingInvisible + 2
                    continue
                }
            }

            // Lesson link: [<filename:text>]
            if (text.startsWith("[<", i)) {
                val closingLink = text.indexOf(
                    ">]",
                    startIndex = i + 2
                )

                if (closingLink != -1) {
                    val linkContent = text.substring(
                        startIndex = i + 2,
                        endIndex = closingLink
                    )

                    val colonIndex = linkContent.indexOf(':')

                    if (colonIndex != -1) {
                        val fileName = linkContent.substring(
                            startIndex = 0,
                            endIndex = colonIndex
                        )

                        val displayText = linkContent.substring(
                            startIndex = colonIndex + 1
                        )

                        if (onLessonLinkClick != null) {

                            val link = LinkAnnotation.Clickable(
                                tag = fileName,
                                styles = TextLinkStyles(
                                    style = SpanStyle(
                                        color = LinkTextColor,
                                        textDecoration = TextDecoration.Underline
                                    )
                                )
                            ) { annotation ->

                                val clickedFileName =
                                    (annotation as LinkAnnotation.Clickable).tag

                                onLessonLinkClick(clickedFileName)
                            }

                            withLink(link) {
                                var j = 0

                                while (j < displayText.length) {
                                    val char = displayText[j]

                                    if (isChineseCharacter(char)) {
                                        withStyle(
                                            SpanStyle(
                                                fontFamily = chineseFontFamily
                                            )
                                        ) {
                                            append(char)
                                        }
                                    } else {
                                        append(char)
                                    }

                                    j++
                                }
                            }

                        } else {
                            append(displayText)
                        }

                        i = closingLink + 2
                        continue
                    }
                }
            }

            // Text inside [ ... ]
            if (text[i] == '[') {
                val closingBracket = text.indexOf(
                    ']',
                    startIndex = i + 1
                )

                if (closingBracket != -1) {
                    val bracketContent = text.substring(
                        startIndex = i + 1,
                        endIndex = closingBracket
                    )

                    appendHighlightedText(
                        text = bracketContent,
                        color = PrimaryBracketTextColor,
                        chineseFontFamily = chineseFontFamily
                    )

                    i = closingBracket + 1
                    continue
                }
            }

            // Text inside { ... }
            if (text[i] == '{') {
                val closingBracket = text.indexOf(
                    '}',
                    startIndex = i + 1
                )

                if (closingBracket != -1) {
                    val bracketContent = text.substring(
                        startIndex = i + 1,
                        endIndex = closingBracket
                    )

                    appendHighlightedText(
                        text = bracketContent,
                        color = SecondaryBracketTextColor,
                        chineseFontFamily = chineseFontFamily
                    )

                    i = closingBracket + 1
                    continue
                }
            }

            // Normal Chinese character
            val char = text[i]

            if (isChineseCharacter(char)) {
                withStyle(
                    SpanStyle(
                        fontFamily = chineseFontFamily
                    )
                ) {
                    append(char)
                }
            } else {
                append(char)
            }

            i++
        }
    }
}

fun isChineseCharacter(c: Char): Boolean {
    val block = Character.UnicodeBlock.of(c)

    return block === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS ||
            block === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A ||
            block === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B ||
            block === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
}
