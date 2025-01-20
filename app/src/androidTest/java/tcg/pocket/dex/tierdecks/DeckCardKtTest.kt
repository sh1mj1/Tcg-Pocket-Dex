package tcg.pocket.dex.tierdecks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class DeckCardKtTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun show_expandedIcon_when_click_not_expandedIcon() {
        composeTestRule.setContent {
            var expanded by remember { mutableStateOf(false) }

            DeckCard(
                deckId = "",
                onCardClick = {},
                rank = 1,
                deckName = "deckName",
                winRate = "winRate",
                share = "share",
                pokemonImageUrls = emptyList(),
                expanded = expanded,
                onExpandClick = { expanded = !expanded },
            )
        }
        val notExpandedIcon = composeTestRule.onNodeWithContentDescription("not expanded icon")
        val expandedIcon = composeTestRule.onNodeWithContentDescription("expanded icon")

        notExpandedIcon.performClick()
        composeTestRule.waitForIdle()

        notExpandedIcon.assertIsNotDisplayed()
        expandedIcon.assertIsDisplayed()
    }

    @Test
    fun show_not_expandedIcon_when_click_expandedIcon() {
        composeTestRule.setContent {
            var expanded by remember { mutableStateOf(true) }

            DeckCard(
                deckId = "",
                rank = 1,
                deckName = "deckName",
                winRate = "winRate",
                share = "share",
                pokemonImageUrls = emptyList(),
                expanded = expanded,
                onExpandClick = { expanded = !expanded },
                onCardClick = {},
            )
        }
        val notExpandedIcon = composeTestRule.onNodeWithContentDescription("not expanded icon")
        val expandedIcon = composeTestRule.onNodeWithContentDescription("expanded icon")

        expandedIcon.performClick()
        composeTestRule.waitForIdle()

        notExpandedIcon.assertIsDisplayed()
        expandedIcon.assertIsNotDisplayed()
    }
}
