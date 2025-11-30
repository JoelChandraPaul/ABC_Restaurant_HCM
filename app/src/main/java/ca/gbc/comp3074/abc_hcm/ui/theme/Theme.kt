package ca.gbc.comp3074.abc_hcm.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// ============================================
// ABC Restaurant HCM Theme
// ============================================

// Light Theme Color Scheme
private val LightColorScheme = lightColorScheme(
    // Primary Colors
    primary = OrangePrimary,
    onPrimary = Color.White,
    primaryContainer = OrangeContainer,
    onPrimaryContainer = OrangePrimaryDark,

    // Secondary Colors
    secondary = TealSecondary,
    onSecondary = Color.White,
    secondaryContainer = TealContainer,
    onSecondaryContainer = TealSecondaryDark,

    // Tertiary Colors
    tertiary = AmberTertiary,
    onTertiary = Color.White,
    tertiaryContainer = AmberContainer,
    onTertiaryContainer = Color(0xFF5D4037),

    // Background & Surface
    background = BackgroundLight,
    onBackground = TextPrimary,
    surface = SurfaceLight,
    onSurface = TextPrimary,
    surfaceVariant = BackgroundVariant,
    onSurfaceVariant = TextSecondary,

    // Error
    error = ErrorRed,
    onError = Color.White,
    errorContainer = ErrorContainer,
    onErrorContainer = Color(0xFFB71C1C),

    // Outline & Other
    outline = Gray400,
    outlineVariant = Gray300,
    scrim = Color.Black.copy(alpha = 0.32f),
    inverseSurface = Gray800,
    inverseOnSurface = Color.White,
    inversePrimary = OrangePrimaryLight
)

// Dark Theme Color Scheme
private val DarkColorScheme = darkColorScheme(
    // Primary Colors
    primary = OrangePrimaryDarkTheme,
    onPrimary = Gray900,
    primaryContainer = OrangePrimaryDark,
    onPrimaryContainer = OrangeContainer,

    // Secondary Colors
    secondary = TealSecondaryDarkTheme,
    onSecondary = Gray900,
    secondaryContainer = TealSecondaryDark,
    onSecondaryContainer = TealContainer,

    // Tertiary Colors
    tertiary = AmberTertiary,
    onTertiary = Gray900,
    tertiaryContainer = Color(0xFF795548),
    onTertiaryContainer = AmberContainer,

    // Background & Surface
    background = BackgroundDark,
    onBackground = Color.White,
    surface = SurfaceDark,
    onSurface = Color.White,
    surfaceVariant = Gray800,
    onSurfaceVariant = Gray400,

    // Error
    error = Color(0xFFEF5350),
    onError = Gray900,
    errorContainer = Color(0xFFB71C1C),
    onErrorContainer = ErrorContainer,

    // Outline & Other
    outline = Gray600,
    outlineVariant = Gray700,
    scrim = Color.Black.copy(alpha = 0.6f),
    inverseSurface = Gray200,
    inverseOnSurface = Gray900,
    inversePrimary = OrangePrimary
)

// Custom Shapes
private val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

@Composable
fun HCMTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Disable dynamic colors to maintain brand consistency
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
