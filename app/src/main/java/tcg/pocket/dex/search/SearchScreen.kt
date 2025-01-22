package tcg.pocket.dex.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

@Composable
fun SearchScreen(modifier: Modifier = Modifier) {
    // TODO: Implement search screen
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier.fillMaxSize(),
    ) {
        Text(
            text = "Search Screen",
            modifier = modifier,
            style = MaterialTheme.typography.displayLarge,
        )
    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    TcgPocketDexTheme {
        SearchScreen()
    }
}
