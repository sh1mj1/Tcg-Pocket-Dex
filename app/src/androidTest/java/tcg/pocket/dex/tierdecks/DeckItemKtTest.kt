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

class DeckItemKtTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun show_expandedIcon_when_click_not_expandedIcon() {
        composeTestRule.setContent {
            var expanded by remember { mutableStateOf(false) }

            DeckItem(
                information = fakeDeckInformation,
                onCardClick = {},
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
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

            DeckItem(
                information = fakeDeckInformation,
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
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
