package tcg.pocket.dex.tierdecks

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
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
import tcg.pocket.dex.ui.theme.TcgPocketDexTheme

// TODO: viewModel
val deckItemsState =
    mutableStateListOf<DeckItemState>().apply {
        addAll(fakeDecksInformation.map { DeckItemState(it) })
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TierDecksScreen() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("TCG Pocket Dex") },
                navigationIcon = {
                    IconButton(onClick = {
                        // TODO: navigate to search screen
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        // TODO: for the setting of the theme
                    }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        DeckList(
            deckItemsState = deckItemsState,
            onExpandDeck = { deckItemState, expanded ->
                deckItemState.toggleExpanded()
            },
            onDeckItemClick = {},
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
        )
    }
}

@Preview
@Composable
private fun TireDecksScreenPreview() {
    TcgPocketDexTheme {
        TierDecksScreen()
    }
}
