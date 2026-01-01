package tcg.pocket.dex.deckdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tcg.pocket.dex.common.UiState
import tcg.pocket.dex.deckdetail.components.AllPokemonGrid
import tcg.pocket.dex.deckdetail.components.DeckHeroCarousel
import tcg.pocket.dex.deckdetail.components.DeckInfoSection
import tcg.pocket.dex.deckdetail.components.KeyPokemonGrid
import tcg.pocket.dex.deckdetail.components.StatChip

@Composable
fun DeckDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: DeckDetailViewModel,
    onCardClick: (String) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier.fillMaxSize(),
    ) {
        when (val state = uiState) {
            is UiState.Loading -> LoadingState(modifier)
            is UiState.Error -> ErrorState(state.message, viewModel::retry, modifier)
            is UiState.Success -> {
                if (state.data.pokemonCards.isEmpty()) {
                    EmptyState(modifier)
                } else {
                    DeckDetailContent(state.data, onCardClick, modifier)
                }
            }
        }
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        androidx.compose.foundation.layout.Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp),
            )
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "No Pokémon information for this deck",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun DeckDetailContent(
    data: DeckDetailData,
    onCardClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            DeckHeroCarousel(pokemonCards = data.pokemonCards)
        }

        item {
            DeckInfoSection(
                deckName = data.deck.deckName,
                pokemonCards = data.pokemonCards,
            )
        }

        item {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                StatChip(
                    label = "Win Rate",
                    value = data.deck.winRate,
                    modifier = Modifier.weight(1f),
                )
                StatChip(
                    label = "Usage",
                    value = data.deck.usageShare,
                    modifier = Modifier.weight(1f),
                )
            }
        }

        item {
            Text(
                text = "━━━ KEY POKÉMON ━━━━━━━━━━━━━━━",
                style = MaterialTheme.typography.labelLarge,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        item {
            KeyPokemonGrid(
                pokemonCards = data.pokemonCards,
                onCardClick = onCardClick,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }

        item {
            Text(
                text = "━━━ ALL POKÉMON ━━━━━━━━━━━━━━━",
                style = MaterialTheme.typography.labelLarge,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        item {
            AllPokemonGrid(
                pokemonCards = data.pokemonCards,
                onCardClick = onCardClick,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }
    }
}
