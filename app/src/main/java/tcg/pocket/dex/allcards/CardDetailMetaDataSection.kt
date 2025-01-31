package tcg.pocket.dex.allcards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import tcg.pocket.dex.tierdecks.fakeCardDetail
import tcg.pocket.dex.tierdecks.temporalPokemonCardPlaceholderDrawable
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

@Composable
fun CardDetailMetaDataSection(
    cardDetail: CardDetail,
    modifier: Modifier = Modifier,
) {
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
                        .weight(0.8f),
                placeholder = painterResource(temporalPokemonCardPlaceholderDrawable),
            )
            Spacer(modifier = Modifier.width(16.dp))

            CardAttribute(
                cardDetail = cardDetail,
                modifier = Modifier.weight(1.2f),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CardDetailMetaDataSectionPreview() {
    TcgPocketDexTheme {
        CardDetailMetaDataSection(
            cardDetail = fakeCardDetail,
        )
    }
}
