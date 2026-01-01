package tcg.pocket.dex.deckdetail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import tcg.pocket.dex.R
import tcg.pocket.dex.allcards.CardDetail

@Composable
fun KeyPokemonGrid(
    pokemonCards: List<CardDetail>,
    onCardClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val columns = 2
    val itemHeight = 200.dp
    val rows = (pokemonCards.size + columns - 1) / columns
    val gridHeight = itemHeight * rows

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier.height(gridHeight),
        userScrollEnabled = false,
    ) {
        items(pokemonCards.size) { index ->
            val card = pokemonCards[index]
            Column(
                modifier =
                    Modifier
                        .padding(4.dp)
                        .clickable { onCardClick(card.name) },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AsyncImage(
                    model = card.imageUrl,
                    contentDescription = card.name,
                    contentScale = ContentScale.Fit,
                    placeholder = painterResource(R.drawable.pocket_dex_card_image),
                    error = painterResource(R.drawable.tcg_pocket_unknown),
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                )
                Text(
                    text = card.name,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp),
                )
                Text(
                    text = if (card.hp.isNotBlank()) "${card.hp} HP" else "-",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
fun AllPokemonGrid(
    pokemonCards: List<CardDetail>,
    onCardClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val columns = 4
    val itemHeight = 120.dp
    val rows = (pokemonCards.size + columns - 1) / columns
    val gridHeight = itemHeight * rows

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier.height(gridHeight),
        userScrollEnabled = false,
    ) {
        items(pokemonCards.size) { index ->
            val card = pokemonCards[index]
            Column(
                modifier =
                    Modifier
                        .padding(4.dp)
                        .clickable { onCardClick(card.name) },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    AsyncImage(
                        model = card.imageUrl,
                        contentDescription = card.name,
                        contentScale = ContentScale.Fit,
                        placeholder = painterResource(R.drawable.pocket_dex_card_image),
                        error = painterResource(R.drawable.tcg_pocket_unknown),
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(80.dp),
                    )
                }
                Text(
                    text = card.name,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }
    }
}
