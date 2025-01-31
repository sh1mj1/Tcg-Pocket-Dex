package tcg.pocket.dex.allcards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tcg.pocket.dex.component.PocketDexSectionHeader
import tcg.pocket.dex.tierdecks.fakeCardsData
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

@Composable
fun RelatedCardsSection(
    relatedCards: List<CardData>,
    onCardClick: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        PocketDexSectionHeader(
            text = "Related Cards",
        )

        Surface(
            tonalElevation = 2.dp,
            shadowElevation = 2.dp,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
        ) {
            LazyRow {
                items(relatedCards) { card ->
                    Column {
                        CardItem(
                            card = card,
                            onClick = onCardClick,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RelatedCardSectionPreview() {
    TcgPocketDexTheme {
        RelatedCardsSection(
            relatedCards = fakeCardsData.subList(0, 5),
            onCardClick = {},
        )
    }
}
