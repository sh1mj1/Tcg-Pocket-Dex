package tcg.pocket.dex.tierdecks

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

@Composable
fun DeckList(
    deckItems: List<DeckInformation>,
    onDeckItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(8.dp),
    ) {
        items(
            items = deckItems,
            key = { deckItem -> deckItem.simple.deckId },
        ) { deckItem ->
            var expanded by rememberSaveable { mutableStateOf(false) }

            DeckItem(
                information = deckItem,
                expanded = expanded,
                onExpandClick = { expanded = !expanded },
                onCardClick = onDeckItemClick,
            )
        }
    }
}

@Preview
@Composable
private fun DeckListPreview() {
    TcgPocketDexTheme {
        DeckList(
            deckItems = fakeDecksInformation,
            onDeckItemClick = { },
        )
    }
}
