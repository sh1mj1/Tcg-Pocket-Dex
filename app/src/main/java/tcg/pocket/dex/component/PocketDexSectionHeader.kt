package tcg.pocket.dex.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

@Composable
fun PocketDexSectionHeader(
    text: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
    ) {
        Text(
            text = text,
            style =
                MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
            modifier = Modifier.padding(4.dp),
        )
    }
}

@Preview
@Composable
private fun PocketDexSectionHeaderPreview() {
    TcgPocketDexTheme {
        PocketDexSectionHeader(
            text = "Related Decks",
        )
    }
}
