package com.mellowingfactory.sleepology.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mellowingfactory.sleepology.R

val fontFamily = FontFamily(
    Font(R.font.applegothic_thin, FontWeight.Thin),
    Font(R.font.applegothic_ultralight, FontWeight.ExtraLight),
    Font(R.font.applegothic_light, FontWeight.Light),
    Font(R.font.applegothic_medium, FontWeight.Medium),
    Font(R.font.applegothic_regular, FontWeight.Normal),
    Font(R.font.applegothic_semibold, FontWeight.SemiBold),
    Font(R.font.applegothic_bold, FontWeight.Bold),
    Font(R.font.applegothic_extrabold, FontWeight.ExtraBold),
    Font(R.font.applegothic_huge, FontWeight.Black)
)

val fontBold50 = TextStyle(fontWeight = FontWeight.Bold, fontFamily = fontFamily, fontSize = 50.sp)
val fontBold38 = TextStyle(fontWeight = FontWeight.Bold, fontFamily = fontFamily, fontSize = 38.sp)
val fontBold30 = TextStyle(fontWeight = FontWeight.Bold, fontFamily = fontFamily, fontSize = 30.sp)
val fontBold28 = TextStyle(fontWeight = FontWeight.Bold, fontFamily = fontFamily, fontSize = 28.sp)
val fontBold26 = TextStyle(fontWeight = FontWeight.Bold, fontFamily = fontFamily, fontSize = 26.sp)
val fontBold22 = TextStyle(fontWeight = FontWeight.Bold, fontFamily = fontFamily, fontSize = 22.sp)
val fontBold20 = TextStyle(fontWeight = FontWeight.Bold, fontFamily = fontFamily, fontSize = 20.sp)
val fontBold18 = TextStyle(fontWeight = FontWeight.Bold, fontFamily = fontFamily, fontSize = 18.sp)
val fontBold16 = TextStyle(fontWeight = FontWeight.Bold, fontFamily = fontFamily, fontSize = 16.sp)

val fontSemiBold20 = TextStyle(fontWeight = FontWeight.Bold, fontFamily = fontFamily, fontSize = 20.sp)

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontFamily = FontFamily.Default,
        fontSize = 16.sp
    )

    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)