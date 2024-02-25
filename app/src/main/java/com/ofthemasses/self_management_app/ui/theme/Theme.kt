package com.ofthemasses.self_management_app.ui.theme

import android.app.Activity
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView

val ColorScheme =
    lightColorScheme(
        background = MorningBackground,
        primary = MorningPrimary,
        secondary = MorningSecondary,
        tertiary = MorningTertiary,
        outlineVariant = MorningOutline)

@Composable
fun SelfmanagementappTheme(content: @Composable () -> Unit) {
  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as Activity).window
      window.statusBarColor = ColorScheme.primary.toArgb()
    }
  }

  MaterialTheme(colorScheme = ColorScheme, typography = Typography, content = content)
}
