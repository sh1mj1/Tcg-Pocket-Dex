package tcg.pocket.dex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import tcg.pocket.dex.deckdetail.DeckDetailScreen
import tcg.pocket.dex.navigation.NavigationManager
import tcg.pocket.dex.navigation.Screen
import tcg.pocket.dex.search.SearchScreen
import tcg.pocket.dex.setting.SettingScreen
import tcg.pocket.dex.tierdecks.TierDecksScreen
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val navigationManager by viewModels<NavigationManager>()
        setContent {
            TcgPocketDexTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    BackHandler(enabled = navigationManager.backstack.size > 1) {
                        navigationManager.navigateBack()
                    }
                    when (val currentScreen = navigationManager.currentScreen) {
                        is Screen.TierDecks ->
                            TierDecksScreen(
                                onSearchClicked = { navigationManager.navigateTo(Screen.Search) },
                                onSettingClicked = { navigationManager.navigateTo(Screen.Setting) },
                                onDeckClicked = { deckId ->
                                    navigationManager.navigateTo(Screen.DeckDetail(deckId))
                                },
                            )

                        is Screen.Search -> SearchScreen()
                        is Screen.Setting -> SettingScreen()
                        is Screen.DeckDetail -> DeckDetailScreen(deckId = currentScreen.deckId)
                    }
                }
            }
        }
    }
}
