package tcg.pocket.dex.tierdecks

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class DeckListKtTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleDeckItemsState =
        fakeDecksInformation.map {
            DeckItemState(it)
        }

    @Test
    fun deckList_expandItem() {
        composeTestRule.setContent {
            DeckList(
                deckItemsState = sampleDeckItemsState,
                onExpandDeck = { itemState, expanded -> itemState.toggleExpanded() },
                onDeckItemClick = {},
            )
        }

        composeTestRule.onAllNodesWithContentDescription("not expanded icon").onFirst().performClick()

        composeTestRule.onNodeWithContentDescription("expanded icon").assertIsDisplayed()
    }
}
