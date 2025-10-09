package tcg.pocket.dex.allcards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import tcg.pocket.dex.repo.allcards.FakeCardsRepo
import tcg.pocket.dex.tierdecks.temporalPokemonCardPlaceholderDrawable
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

@Composable
fun CardDetailMetaDataSection(
    cardDetail: CardDetail,
    modifier: Modifier = Modifier,
) {
    // TODO: Find a way to avoid calculating density or height using internal variables
    var imageHeightPx by rememberSaveable { mutableStateOf(0) }
    val density = LocalDensity.current

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = cardDetail.imageUrl,
                contentDescription = null,
                modifier =
                    Modifier
                        .weight(0.8f)
                        .onSizeChanged { size ->
                            imageHeightPx = size.height
                        },
                placeholder = painterResource(temporalPokemonCardPlaceholderDrawable),
            )
            Spacer(modifier = Modifier.width(16.dp))

            val imageHeightDp = with(density) { imageHeightPx.toDp() }

            CardAttribute(
                cardDetail = cardDetail,
                modifier =
                    Modifier
                        .weight(1.2f)
                        .defaultMinSize(minHeight = imageHeightDp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CardDetailMetaDataSectionPreview() {
    TcgPocketDexTheme {
        CardDetailMetaDataSection(
            cardDetail = FakeCardsRepo.fakeCardDetail,
        )
    }
}
