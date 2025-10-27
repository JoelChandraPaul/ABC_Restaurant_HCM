package ca.gbc.comp3074.abc_hcm.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ------------ Color Palette ------------

private val PurplePrimary = Color(0xFF6C63FF)
private val PurpleContainer = Color(0xFFE8E7FF)
private val TealAccent = Color(0xFF00BFA6)
private val ErrorRed = Color(0xFFE53935)
private val BackgroundLight = Color(0xFFF7F5FA)
private val TextDark = Color(0xFF1B1B1B)

// ------------ Light Color Scheme ------------
private val LightColors = lightColorScheme(
    primary = PurplePrimary,
    onPrimary = Color.White,
    primaryContainer = PurpleContainer,
    secondary = TealAccent,
    onSecondary = Color.White,
    error = ErrorRed,
    background = BackgroundLight,
    onBackground = TextDark,
    surface = Color.White,
    onSurface = TextDark
)

// ------------ Dark Color Scheme ------------
private val DarkColors = darkColorScheme(
    primary = PurplePrimary,
    onPrimary = Color.White,
    secondary = TealAccent,
    error = ErrorRed
)

// ------------ Theme Wrapper ------------
@Composable
fun HCMTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    //val colors = if (darkTheme) DarkColors else LightColors
    val BrandBlue = Color(0xFF1565C0)
    val BrandGold = Color(0xFFFFC107)
    val BrandSurface = Color(0xFFF7F8FA)


    MaterialTheme(
        colorScheme = lightColorScheme(primary = BrandBlue, secondary = BrandGold, surface = BrandSurface),
        typography = Typography(),
        content = content
    )
}
