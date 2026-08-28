package com.example.gram.ui.lessoncontentscreen.components.leftbar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gram.ui.theme.ButtonBackgroundColor
import com.example.gram.ui.theme.ButtonForegroundColor
import com.example.gram.ui.theme.DisabledButtonBackgroundColor
import com.example.gram.ui.theme.DisabledButtonForegroundColor

@Composable
fun BaseButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = ButtonBackgroundColor,
            contentColor = ButtonForegroundColor,
            disabledContainerColor = DisabledButtonBackgroundColor,
            disabledContentColor = DisabledButtonForegroundColor
        ),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
            .height(66.dp)
            .width(20.dp)
    ) { content() }
}
