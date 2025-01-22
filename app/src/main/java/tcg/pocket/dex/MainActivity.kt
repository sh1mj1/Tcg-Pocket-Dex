package tcg.pocket.dex

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import tcg.pocket.dex.search.SearchActivity
import tcg.pocket.dex.tierdecks.TierDecksScreen
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TcgPocketDexTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TierDecksScreen(
                        onSearchClicked = {
                            startActivity<SearchActivity>()
                        },
                    )
                }
            }
        }
    }
}

inline fun <reified T : Activity> Context.startActivity(argusBuilder: Intent.() -> Unit = {}) {
    startActivity(Intent(this, T::class.java).apply(argusBuilder))
}
