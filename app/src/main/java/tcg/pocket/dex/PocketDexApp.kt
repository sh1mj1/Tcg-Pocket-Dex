package tcg.pocket.dex

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import tcg.pocket.dex.allcards.AllCardsScreen
import tcg.pocket.dex.extensionpacks.ExtensionPacksScreen
import tcg.pocket.dex.navigation.AllCards
import tcg.pocket.dex.navigation.ExpansionPacks
import tcg.pocket.dex.navigation.TierDecks
import tcg.pocket.dex.navigation.bottomBarScreens
import tcg.pocket.dex.tierdecks.DeckItemState
import tcg.pocket.dex.tierdecks.DeckList
import tcg.pocket.dex.tierdecks.PocketDexTopBar
import tcg.pocket.dex.tierdecks.fakeDecksInformation
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

val deckItemsState =
    mutableStateListOf<DeckItemState>().apply {
        addAll(fakeDecksInformation.map { DeckItemState(it) })
    }

@Composable
fun PocketDexApp(
    openUrl: () -> Unit = {},
    onSearchClicked: () -> Unit = {},
    onSettingClicked: () -> Unit = {},
) {
    TcgPocketDexTheme {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination

        val currentScreen =
            bottomBarScreens.find { it.route == currentDestination?.route } ?: TierDecks

        Scaffold(
            topBar = {
                PocketDexTopBar(
                    openUrl = openUrl,
                    onSearchClicked = onSearchClicked,
                    onSettingClicked = onSettingClicked,
                )
            },
            bottomBar = {
                PocketDexBottomBar(
                    allScreens = bottomBarScreens,
                    onTabSelected = { newScreen ->
                        navController.navigateSingleTopTo(newScreen.route)
                    },
                    currentScreen = currentScreen,
                )
            },
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = TierDecks.route,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable(route = TierDecks.route) {
                    DeckList(
                        deckItemsState = deckItemsState,
                        onDeckItemClick = { /* TODO */ },
                        onExpandDeck = { _, _ ->
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

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id,
        ) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }

@Preview(showBackground = true)
@Composable
private fun PocketDexAppPreview() {
    PocketDexApp()
}
