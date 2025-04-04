package tcg.pocket.dex.allcards

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tcg.pocket.dex.component.CardItem
import tcg.pocket.dex.repo.allcards.FakeCardsRepo
import tcg.pocket.dex.tierdecks.fakeCardsData
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

@Composable
fun AllCardsScreen(
    modifier: Modifier = Modifier,
    viewModel: AllCardsViewModel,
    onCardClick: (String) -> Unit = {},
) {
    val allCardsState by viewModel.cardsState.collectAsStateWithLifecycle()
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 100.dp),
        modifier =
            modifier
                .fillMaxSize()
                .padding(8.dp),
    ) {
        items(allCardsState.size) { index ->
            val card = allCardsState[index]
            CardItem(
                card = card,
                onClick = { onCardClick(card.Id) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TierDecksScreenPreview() {
    TcgPocketDexTheme {
        AllCardsScreen(
            viewModel = AllCardsViewModel(cardsRepo = FakeCardsRepo()),
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
