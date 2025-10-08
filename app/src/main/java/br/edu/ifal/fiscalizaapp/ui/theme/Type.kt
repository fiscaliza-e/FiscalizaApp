package br.edu.ifal.fiscalizaapp.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Typography

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.sp
import br.edu.ifal.fiscalizaapp.R


val interFont = Font(R.font.inter).toFontFamily()
val manropeFont = Font(R.font.manrope).toFontFamily()

val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = interFont,
        fontWeight = FontWeight.W600,
        fontSize = 17.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = interFont,
        fontSize = 14.sp,
        color = Color.DarkGray,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)