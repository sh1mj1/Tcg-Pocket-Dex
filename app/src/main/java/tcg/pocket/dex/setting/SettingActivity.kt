package tcg.pocket.dex.setting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

class SettingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TcgPocketDexTheme {
                SettingScreen(
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
