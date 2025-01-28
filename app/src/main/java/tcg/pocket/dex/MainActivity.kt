package tcg.pocket.dex

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import tcg.pocket.dex.navigation.BottomNavigationManager
import tcg.pocket.dex.navigation.NavigationManager
import tcg.pocket.dex.navigation.Screen
import tcg.pocket.dex.search.SearchScreen
import tcg.pocket.dex.setting.SettingScreen
import tcg.pocket.dex.tierdecks.MainScreen
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val navigationManager: NavigationManager by viewModels {
            NavigationManager.factory()
        }

        val bottomNavigationManager: BottomNavigationManager by viewModels {
            BottomNavigationManager.factory(
                onBackStackIsEmpty = ::finish,
            )
        }
        setContent {
            TcgPocketDexTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    BackHandler(enabled = navigationManager.navigateBackAvailable()) {
                        navigationManager.navigateBack()
                    }

                    when (
                        val currentScreen =
                            try {
                                navigationManager.currentScreen
                            } catch (e: NoSuchElementException) {
                                finish()
                            }
                    ) {
                        is Screen.Search -> SearchScreen()
                        is Screen.Setting -> SettingScreen()
                        is Screen.DeckDetail -> DeckDetailScreen(deckId = currentScreen.deckId)

                        is Screen.BottomNavigation.AllCards -> {}
                        is Screen.BottomNavigation.ExtensionPacks -> {}
                        is Screen.BottomNavigation.TierDecks -> {
                            MainScreen(
                                bottomNavigationManager = bottomNavigationManager,
                                onSearchClicked = { navigationManager.navigateTo(Screen.Search) },
                                onSettingClicked = { navigationManager.navigateTo(Screen.Setting) },
                                onDeckClicked = { deckId ->
                                    navigationManager.navigateTo(Screen.DeckDetail(deckId))
                                },
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
