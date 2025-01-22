package tcg.pocket.dex.search

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

class SearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TcgPocketDexTheme {
                SearchScreen(
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
