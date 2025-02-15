package tcg.pocket.dex.allcards

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tcg.pocket.dex.component.CardItem
import tcg.pocket.dex.tierdecks.fakeCardsData
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

@Composable
fun AllCardsScreen(
    modifier: Modifier = Modifier,
    viewModel: AllCardsViewModel = AllCardsViewModel(),
    onCardClick: (String) -> Unit = {},
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 100.dp),
        modifier =
            modifier
                .fillMaxSize()
                .padding(8.dp),
    ) {
        viewModel.cardsState.value.apply {
            items(size) { index ->
                val card = this@apply[index]
                CardItem(
                    card = card,
                    onClick = { onCardClick(card.Id) },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TierDecksScreenPreview() {
    TcgPocketDexTheme {
        AllCardsScreen(
            viewModel = AllCardsViewModel(),
            onCardClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CardItemPreview() {
    TcgPocketDexTheme {
        CardItem(
            card = fakeCardsData[0],
            onClick = {},
        )
    }
}

data class CardData(
    val Id: String = "",
    val name: String,
    val imageUrl: String,
    val rarityUrl: String,
    val typeUrl: String,
)
