package tcg.pocket.dex

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import tcg.pocket.dex.allcards.AllCardsScreen
import tcg.pocket.dex.extensionpacks.ExtensionPacksScreen
import tcg.pocket.dex.navigation.AllCards
import tcg.pocket.dex.navigation.ExpansionPacks
import tcg.pocket.dex.navigation.NavDestination
import tcg.pocket.dex.navigation.TierDecks
import tcg.pocket.dex.tierdecks.DeckItemState
import tcg.pocket.dex.tierdecks.DeckList
import tcg.pocket.dex.tierdecks.fakeDecksInformation
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

val deckItemsState =
    mutableStateListOf<DeckItemState>().apply {
        addAll(fakeDecksInformation.map { DeckItemState(it) })
    }

@Composable
fun PocketDexApp(modifier: Modifier = Modifier) {
    TcgPocketDexTheme {
        var currentScreen: NavDestination by remember { mutableStateOf(TierDecks) }
        val navController = rememberNavController()
        Scaffold { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = TierDecks.route,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable(route = TierDecks.route) {
                    DeckList(
                        deckItemsState = deckItemsState,
                        onDeckItemClick = { /* TODO */ },
                        onExpandDeck = { deckItemStata, _ ->
                            // TODO
                        },
                    )
                }
                composable(route = AllCards.route) {
                    AllCardsScreen()
                }
                composable(route = ExpansionPacks.route) {
                    ExtensionPacksScreen()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PocketDexAppPreview() {
    PocketDexApp()
}
