package tcg.pocket.dex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import tcg.pocket.dex.tierdecks.TierDecksScreen
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TcgPocketDexTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TierDecksScreen()
                }
            }
        }
    }
}
