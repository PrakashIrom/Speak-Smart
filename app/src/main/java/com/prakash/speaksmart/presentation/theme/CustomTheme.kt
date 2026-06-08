package com.prakash.speaksmart.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color

object SpeakSmartTheme {
    val Surface = Color(0xFF131313)
    val SurfaceContainer = Color(0xFF201F1F)
    val SurfaceContainerHigh = Color(0xFF2A2A2A)
    val OnSurface = Color(0xFFE5E2E1)
    val OnSurfaceVariant = Color(0xFFB9CBBC)

    // Accents
    val EmeraldGreen = Color(0xFF00FF9D)
    val CoolBlue = Color(0xFF00D1FF)
    val PulsingCrimson = Color(0xFFFF2D55)

    // Typography
    val HeadlineMd = TextStyle(
        fontFamily = FontFamily.SansSerif, // Stands in cleanly for Inter
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 32.sp
    )
    val BodyLg = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 18.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 28.sp
    )
    val BodyMd = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp
    )
    val LabelMd = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 20.sp,
        letterSpacing = 0.05.sp
    )
    val LabelSm = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 16.sp
    )

    // Shapes
    val RadiusMedium = RoundedCornerShape(12.dp)
    val RadiusLarge = RoundedCornerShape(16.dp)
}