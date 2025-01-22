package tcg.pocket.dex.setting

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

@Composable
fun SettingScreen(modifier: Modifier = Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier.fillMaxSize(),
    ) {
        Text(
            text = "Setting",
            style = MaterialTheme.typography.displayLarge,
        )
    }
}

@Preview
@Composable
private fun SettingScreenPreview() {
    TcgPocketDexTheme {
        SettingScreen()
    }
}
