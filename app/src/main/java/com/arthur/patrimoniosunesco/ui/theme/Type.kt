package com.arthur.patrimoniosunesco.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography().copy(
    headlineLarge = Typography().headlineLarge.copy(fontWeight = FontWeight.Bold, fontSize = 30.sp),
    headlineSmall = Typography().headlineSmall.copy(fontWeight = FontWeight.Bold),
    titleLarge = Typography().titleLarge.copy(fontWeight = FontWeight.SemiBold),
    bodyLarge = Typography().bodyLarge.copy(lineHeight = 25.sp)
)
