package tcg.pocket.dex.tierdecks

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.lifecycle.viewmodel.compose.viewModel
import org.junit.Rule
import org.junit.Test

class TierDecksScreenKtTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun keep_expanded_state_after_configuration_change0() {
        val restorationTester =
            StateRestorationTester(composeTestRule).apply {
                setContent {
                    val viewModel: TierDecksViewModel = viewModel()
                    TierDecksScreen(
                        viewModel = viewModel,
                        onDeckItemClick = {},
                    )
                }
            }

        // When - 첫 번째 아이템 확장
        composeTestRule.onAllNodesWithContentDescription("not expanded icon").onFirst()
            .performClick()

        // Then - 확장 상태 확인
        composeTestRule.onNodeWithContentDescription("expanded icon").assertIsDisplayed()

        // When - 구성 변경 시뮬레이션
        restorationTester.emulateSavedInstanceStateRestore()

        // Then - 구성 변경 후에도 확장 상태 유지 확인
        composeTestRule.onNodeWithContentDescription("expanded icon").assertIsDisplayed()
    }

    @Test
    fun keep_expanded_state_after_configuration_change() {
        composeTestRule.apply {
            // Given - 초기 UI 설정
            activity.runOnUiThread {
                activity.setContent {
                    val viewModel: TierDecksViewModel = viewModel()
                    TierDecksScreen(
                        viewModel = viewModel,
                        onDeckItemClick = {},
                    )
                }
            }

            // When - 첫 번째 아이템 확장
            onAllNodesWithContentDescription("not expanded icon").onFirst().performClick()
            waitForIdle()

            // Then - 확장 상태 확인
            onNodeWithContentDescription("expanded icon").assertIsDisplayed()

            // When - 구성 변경(화면 회전) 시뮬레이션
            activityRule.scenario.recreate()

            // UI 복원이 완료될 때까지 대기
            waitUntil(timeoutMillis = 5000) {
                activity.runOnUiThread {
                    activity.setContent {
                        val viewModel: TierDecksViewModel = viewModel()
                        TierDecksScreen(
                            viewModel = viewModel,
                            onDeckItemClick = {},
                        )
                    }
                }
                onAllNodesWithContentDescription("expanded icon").fetchSemanticsNodes().isNotEmpty()
            }

            // Then - 구성 변경 후에도 확장 상태 유지 확인
            onNodeWithContentDescription("expanded icon").assertIsDisplayed()
        }
    }
}
