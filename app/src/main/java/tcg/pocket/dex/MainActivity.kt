package tcg.pocket.dex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tcg.pocket.dex.tierdecks.DeckItem
import tcg.pocket.dex.tierdecks.fakePokemonTypeChipDataset
import tcg.pocket.dex.tierdecks.fakeSimpleUrl
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var expanded by rememberSaveable { mutableStateOf(true) }
            TcgPocketDexTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DeckItem(
                        deckId = "deckId",
                        rank = 1,
                        deckName = "Mewtwo ex Gardevoir",
                        winRate = "50.64%",
                        share = "17.57%",
                        pokemonImageUrls =
                            listOf(
                                fakeSimpleUrl(150),
                                fakeSimpleUrl(282),
                            ),
                        cost = 1990,
                        pokemonTypes = fakePokemonTypeChipDataset,
                        expanded = expanded,
                        onExpandClick = { expanded = !expanded },
                        onCardClick = {},
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TcgPocketDexTheme {
        Greeting("Android")
    }
}
