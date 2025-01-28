package tcg.pocket.dex.tierdecks

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tcg.pocket.dex.allcards.AllCardsScreen
import tcg.pocket.dex.extensionpacks.ExtensionPacksScreen
import tcg.pocket.dex.navigation.BottomNavigationManager
import tcg.pocket.dex.navigation.Screen
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

// TODO: viewModel
val deckItemsState =
    mutableStateListOf<DeckItemState>().apply {
        addAll(fakeDecksInformation.map { DeckItemState(it) })
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    bottomNavigationManager: BottomNavigationManager,
    onSearchClicked: () -> Unit = {},
    onSettingClicked: () -> Unit = {},
    onDeckClicked: (String) -> Unit = {},
    openUrl: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "TCG Pocket Dex",
                        modifier =
                            Modifier.clickable(
                                onClick = openUrl,
                            ),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onSearchClicked) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onSettingClicked) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Setting Icon",
                        )
                    }
                },
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(
                        onClick = { bottomNavigationManager.navigateTo(Screen.BottomNavigation.AllCards) },
                        modifier = Modifier.weight(1f),
                    ) {
                        Icon(Icons.AutoMirrored.Filled.List, contentDescription = "All Cards Icon")
                    }

                    IconButton(
                        onClick = { bottomNavigationManager.navigateTo(Screen.BottomNavigation.TierDecks) },
                        modifier = Modifier.weight(1f),
                    ) {
                        Icon(Icons.Default.Star, contentDescription = "Tire Decks Icon")
                    }

                    IconButton(
                        onClick = { bottomNavigationManager.navigateTo(Screen.BottomNavigation.ExtensionPacks) },
                        modifier = Modifier.weight(1f),
                    ) {
                        Icon(
                            Icons.Default.AccountBox,
                            contentDescription = "Extension Packs Icon",
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        BackHandler(enabled = bottomNavigationManager.navigateBackAvailable()) {
            bottomNavigationManager.navigateSingleTopTo(Screen.BottomNavigation.TierDecks)
        }

        when (
            try {
                bottomNavigationManager.currentScreen
            } catch (e: NoSuchElementException) {
                bottomNavigationManager.onBackStackIsEmpty()
            }
        ) {
            is Screen.BottomNavigation.TierDecks -> {
                DeckList(
                    deckItemsState = deckItemsState,
                    onExpandDeck = { deckItemState, _ ->
                        deckItemState.toggleExpanded()
                    },
                    onDeckItemClick = onDeckClicked,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(innerPadding),
                )
            }

            is Screen.BottomNavigation.AllCards ->
                AllCardsScreen(
                    modifier = Modifier.padding(innerPadding),
                )

            is Screen.BottomNavigation.ExtensionPacks ->
                ExtensionPacksScreen(
                    modifier = Modifier.padding(innerPadding),
                )
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    TcgPocketDexTheme {
        MainScreen(
            bottomNavigationManager =
                BottomNavigationManager(
                    onBackStackIsEmpty = {},
                ),
        )
    }
}
