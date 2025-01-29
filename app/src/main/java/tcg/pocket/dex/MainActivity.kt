package tcg.pocket.dex

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TcgPocketDexTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PocketDexApp(
                        openUrl = {
                            openUrl(
                                this,
                                getString(R.string.tcg_pocket_google_play_url),
                            )
                        },
                    )
                }
            }
        }
    }

    private fun openUrl(
        context: Context,
        url: String,
    ) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }
}
