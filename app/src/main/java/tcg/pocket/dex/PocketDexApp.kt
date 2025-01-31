package tcg.pocket.dex

import androidx.compose.animation.AnimatedVisibility
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
import tcg.pocket.dex.allcards.CardDetailScreen
import tcg.pocket.dex.deckdetail.DeckDetailScreen
import tcg.pocket.dex.extensionpacks.ExtensionPacksScreen
import tcg.pocket.dex.navigation.AllCards
import tcg.pocket.dex.navigation.CardDetail
import tcg.pocket.dex.navigation.ExpansionPacks
import tcg.pocket.dex.navigation.Search
import tcg.pocket.dex.navigation.Setting
import tcg.pocket.dex.navigation.TierDeckDetail
import tcg.pocket.dex.navigation.TierDecks
import tcg.pocket.dex.navigation.bottomBarScreens
import tcg.pocket.dex.search.SearchScreenForAllCards
import tcg.pocket.dex.search.SearchScreenForExpansionPacks
import tcg.pocket.dex.search.SearchScreenForTierDecks
import tcg.pocket.dex.setting.SettingScreen
import tcg.pocket.dex.tierdecks.DeckItemState
import tcg.pocket.dex.tierdecks.PocketDexTopBar
import tcg.pocket.dex.tierdecks.TierDecksScreen
import tcg.pocket.dex.tierdecks.fakeCardsData
import tcg.pocket.dex.tierdecks.fakeDecksInformation
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

// TODO: move to viewmodel
val deckItemsState =
    mutableStateListOf<DeckItemState>().apply {
        addAll(fakeDecksInformation.map(::DeckItemState))
    }

val relatedDeckItemState =
    mutableStateListOf<DeckItemState>().apply {
        addAll(fakeDecksInformation.subList(0, 4).map(::DeckItemState))
    }

@Composable
fun PocketDexApp(openUrl: () -> Unit = {}) {
    TcgPocketDexTheme {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination

        val currentScreen =
            bottomBarScreens.find { it.route == currentDestination?.route } ?: TierDecks

        val isTopBarVisible =
            bottomBarScreens.any { destination ->
                currentDestination?.route?.startsWith(destination.route) == true
            }

        val isBottomTabDestination =
            bottomBarScreens.any { destination ->
                currentDestination?.route?.startsWith(destination.route) == true
            }

        Scaffold(
            topBar = {
                AnimatedVisibility(isTopBarVisible) {
                    PocketDexTopBar(
                        openUrl = openUrl,
                        onSearchClicked = {
                            when (currentScreen) {
                                is AllCards -> navController.navigate(Search.routeWithArgs("all_cards"))
                                is TierDecks -> navController.navigate(Search.routeWithArgs("tier_decks"))
                                is ExpansionPacks -> navController.navigate(Search.routeWithArgs("expansion_packs"))
                            }
                        },
                        onSettingClicked = { navController.navigate(Setting.route) },
                    )
                }
            },
            bottomBar = {
                AnimatedVisibility(isBottomTabDestination) {
                    PocketDexBottomBar(
                        allScreens = bottomBarScreens,
                        onTabSelected = { newScreen ->
                            navController.navigateSingleTopTo(newScreen.route)
                        },
                        currentScreen = currentScreen,
                    )
                }
            },
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = TierDecks.route,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable(route = TierDecks.route) {
                    TierDecksScreen(
                        deckItemsState = deckItemsState,
                        onDeckItemClick = { deckId ->
                            navController.navigate(TierDeckDetail.routeWithArgs(deckId))
                        },
                        onExpandDeck = { deckItemState, _ ->
                            deckItemState.toggleExpanded()
                        },
                    )
                }
                composable(route = Setting.route) {
                    SettingScreen()
                }

                composable(route = AllCards.route) {
                    AllCardsScreen(
                        cards = fakeCardsData,
                        onCardClick = {
                            navController.navigate(CardDetail.routeWithArgs(it))
                        },
                    )
                }
                composable(route = ExpansionPacks.route) {
                    ExtensionPacksScreen()
                }
                composable(
                    route = TierDeckDetail.routeWithArgs,
                    arguments = TierDeckDetail.arguments,
                ) { navBackStackEntry ->
                    val deckId = navBackStackEntry.arguments?.getString(TierDeckDetail.DECK_ID_ARG)
                    DeckDetailScreen(deckId = deckId)
                }
                composable(
                    route = Search.routeWithArgs,
                    arguments = Search.arguments,
                ) { navBackStackEntry ->
                    val searchType = navBackStackEntry.arguments?.getString(Search.SEARCH_TYPE_ARG)
                    // TODO: implement SearchScreen for all cards
                    when (searchType) {
                        "tier_decks" -> SearchScreenForTierDecks()
                        "all_cards" -> SearchScreenForAllCards()
                        "expansion_packs" -> SearchScreenForExpansionPacks()
                        else -> error("Unknown search type: $searchType")
                    }
                }
                composable(
                    route = CardDetail.routeWithArgs,
                    arguments = CardDetail.arguments,
                ) { navBackStackEntry ->
                    val cardId = navBackStackEntry.arguments?.getString(CardDetail.CARD_DETAIL_ARG)
                    CardDetailScreen(
                        cardId = cardId,
                        deckItemsState = relatedDeckItemState,
                        onExpandDeck = { deckItemState, _ ->
                            deckItemState.toggleExpanded()
                        },
                        onDeckItemClick = { deckId ->
                            navController.navigate(TierDeckDetail.routeWithArgs(deckId))
                        },
                    )
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
