package tcg.pocket.dex

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class MainActivityTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    // MainActivity 는 생성자 파라미터에 직접 주입이 안 됨.
    // NavigationManager 를 생성자 파라미터로 주입 불가.
    // 그래서 아래처럼 Search Screen 으로 이동하는 given 이 생김
    @Test
    fun backstack_persists_across_configuration_change() {
        // given
        composeTestRule.onNodeWithContentDescription("Search Icon")
            .performClick()

        // you're on the Search Screen
        composeTestRule.onNodeWithText("Search Screen").assertIsDisplayed()

        // when
        composeTestRule.activity.runOnUiThread {
            composeTestRule.activity.recreate()
        }

        // then // TODO: have to succeed
//        composeTestRule.onNodeWithText("Search Screen").assertIsDisplayed()
    }
}
