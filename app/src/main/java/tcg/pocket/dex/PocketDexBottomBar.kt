package tcg.pocket.dex

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tcg.pocket.dex.navigation.BottomNavDestination
import tcg.pocket.dex.navigation.NavDestination
import tcg.pocket.dex.navigation.bottomBarScreens
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme
import java.util.Locale

@Composable
fun PocketDexBottomBar(
    allScreens: List<BottomNavDestination>,
    onTabSelected: (BottomNavDestination) -> Unit,
    currentScreen: NavDestination,
) {
    BottomAppBar(
        actions = {
            allScreens.forEach { screen ->
                PocketDexTab(
                    text = screen.text,
                    icon = screen.icon,
                    onSelected = { onTabSelected(screen) },
                    selected = currentScreen == screen,
                )
            }
        },
    )
}

@Composable
fun PocketDexTab(
    text: String,
    icon: ImageVector,
    onSelected: () -> Unit,
    selected: Boolean,
) {
    val color = MaterialTheme.colorScheme.onSurface
    val durationMillis =
        if (selected) TAB_FADE_IN_ANIMATION_DURATION else TAB_FADE_OUT_ANIMATION_DURATION
    val animSpec =
        remember {
            tween<Color>(
                durationMillis = durationMillis,
                easing = LinearEasing,
                delayMillis = TAB_FADE_IN_ANIMATION_DELAY,
            )
        }

    val tabTintColor by animateColorAsState(
        targetValue = if (selected) color else color.copy(alpha = INACTIVE_TAB_OPACITY),
        animationSpec = animSpec,
        label = "color animation for tab tint",
    )

    Row(
        modifier =
            Modifier
                .padding(16.dp)
                .animateContentSize()
                .selectable(
                    selected = selected,
                    onClick = onSelected,
                    role = Role.Tab,
                    interactionSource = remember { MutableInteractionSource() },
                    indication =
                        ripple(
                            bounded = false,
                            radius = Dp.Unspecified,
                            color = Color.Unspecified,
                        ),
                )
                .clearAndSetSemantics {
                    contentDescription = text
                },
    ) {
        Icon(imageVector = icon, contentDescription = text, tint = tabTintColor)
        if (selected) {
            Spacer(Modifier.width(12.dp))
            Text(text.uppercase(Locale.getDefault()), color = tabTintColor)
        }
    }
}

@Preview
@Composable
private fun PocketDexBottomBarPreview(
    @PreviewParameter(BottomBarScreensParameters::class) currentScreen: BottomNavDestination,
) {
    TcgPocketDexTheme {
        PocketDexBottomBar(
            allScreens = bottomBarScreens,
            onTabSelected = { },
            currentScreen = currentScreen,
        )
    }
}

class BottomBarScreensParameters : PreviewParameterProvider<BottomNavDestination> {
    override val values: Sequence<BottomNavDestination> = bottomBarScreens.asSequence()
}

private const val TAB_FADE_IN_ANIMATION_DURATION = 150
private const val TAB_FADE_IN_ANIMATION_DELAY = 100
private const val TAB_FADE_OUT_ANIMATION_DURATION = 100
private const val INACTIVE_TAB_OPACITY = 0.60f
