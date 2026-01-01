package tcg.pocket.dex.deckdetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import tcg.pocket.dex.R
import tcg.pocket.dex.allcards.CardDetail

@Composable
fun DeckHeroCarousel(
    pokemonCards: List<CardDetail>,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(pageCount = { pokemonCards.size })

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.height(300.dp),
        ) { page ->
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                AsyncImage(
                    model = pokemonCards[page].imageUrl,
                    contentDescription = pokemonCards[page].name,
                    contentScale = ContentScale.Fit,
                    placeholder = painterResource(R.drawable.pocket_dex_card_image),
                    error = painterResource(R.drawable.tcg_pocket_unknown),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            repeat(pokemonCards.size) { index ->
                val color =
                    if (pagerState.currentPage == index) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    }
                Card(
                    modifier =
                        Modifier
                            .padding(4.dp)
                            .size(8.dp),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = color),
                ) {}
            }
        }
    }
}
