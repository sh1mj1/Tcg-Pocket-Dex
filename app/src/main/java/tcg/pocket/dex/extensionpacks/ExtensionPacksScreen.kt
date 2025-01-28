package tcg.pocket.dex.extensionpacks

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ExtensionPacksScreen(modifier: Modifier = Modifier) {
    // TODO: Implement extension packs screen
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier.fillMaxSize(),
    ) {
        Text(
            text = "Extension Packs",
            modifier = modifier,
            style = MaterialTheme.typography.displayLarge,
        )
    }
}
