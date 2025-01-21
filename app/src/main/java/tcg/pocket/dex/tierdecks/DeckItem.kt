package tcg.pocket.dex.tierdecks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

@Composable
fun DeckItem(
    information: DeckInformation,
    expanded: Boolean = false,
    onExpandClick: () -> Unit,
    onCardClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(4.dp)
                .clickable { onCardClick(information.simple.deckId) },
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RankText(
                    rank = information.simple.rank,
                    modifier = Modifier.padding(end = 8.dp),
                )

                PokemonImageList(
                    pokemonImageUrls = information.simple.representativePokemonImageUrls,
                    modifier = Modifier,
                )

                DeckItemInfoContent(
                    deckName = information.simple.deckName,
                    winRate = information.simple.winRate,
                    share = information.simple.share,
                    modifier = Modifier.weight(1f),
                )

                IconButton(
                    modifier = Modifier.padding(0.dp),
                    onClick = onExpandClick,
                ) {
                    if (expanded) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowUp,
                            contentDescription = "expanded icon",
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "not expanded icon",
                        )
                    }
                }
            }

            AnimatedVisibility(expanded) {
                DeckItemDetail(
                    cost = information.detail.cost,
                    pokemonTypes = information.detail.pokemonTypes,
                    modifier = Modifier,
                )
            }
        }
    }
}

@Composable
private fun DeckItemDetail(
    cost: Int,
    pokemonTypes: List<PokemonTypeChipData>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "cost: $cost",
                modifier = Modifier,
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.weight(1f))

            PokemonTypeChipGroup(
                chips = pokemonTypes,
                modifier = modifier,
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = fakeTierDeckDescription,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun RankText(
    rank: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "$rank",
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = modifier,
    )
}

@Composable
private fun PokemonImageList(
    pokemonImageUrls: List<String>,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        pokemonImageUrls.forEach { imageUrl ->
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier =
                    Modifier
                        .size(52.dp)
                        .aspectRatio(1f),
                placeholder = painterResource(temporalPokemonPlaceholderDrawable),
            )
        }
    }
}

@Composable
private fun DeckItemInfoContent(
    deckName: String,
    winRate: String,
    share: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = deckName,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Win(%): $winRate",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Share(%): $share",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview
@Composable
private fun RankTextPreview() {
    TcgPocketDexTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            RankText(rank = 1)
        }
    }
}

@Preview
@Composable
private fun PokemonImageListPreview() {
    TcgPocketDexTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            PokemonImageList(
                pokemonImageUrls =
                    listOf(
                        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/150.png",
                        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/282.png",
                    ),
            )
        }
    }
}

@Preview
@Composable
private fun DeckItemInfoContentPreview() {
    TcgPocketDexTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            DeckItemInfoContent(
                deckName = "Mewtwo ex Gardevoir",
                winRate = "50.67",
                share = "17.57",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DeckItemPreview(
    @PreviewParameter(DeckCardPreviewParameters::class) expanded: Boolean,
) {
    TcgPocketDexTheme {
        DeckItem(
            information = fakeDeckInformation,
            expanded = expanded,
            onExpandClick = {},
            onCardClick = {},
        )
    }
}

class DeckCardPreviewParameters : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean> = sequenceOf(true, false)
}
