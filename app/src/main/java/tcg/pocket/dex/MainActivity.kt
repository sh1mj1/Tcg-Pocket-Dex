package tcg.pocket.dex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import tcg.pocket.dex.tierdecks.DeckItem
import tcg.pocket.dex.tierdecks.fakeDeckInformation
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
                        information = fakeDeckInformation,
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
