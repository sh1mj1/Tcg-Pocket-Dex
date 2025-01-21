package tcg.pocket.dex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import tcg.pocket.dex.tierdecks.DeckList
import tcg.pocket.dex.tierdecks.fakeDecksInformation
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TcgPocketDexTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DeckList(
                        deckItems = fakeDecksInformation,
                        onDeckItemClick = { },
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}
