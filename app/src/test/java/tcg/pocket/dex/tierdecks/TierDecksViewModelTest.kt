package tcg.pocket.dex.tierdecks

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import tcg.pocket.dex.common.UiState
import tcg.pocket.dex.repo.decks.DecksRepo
import tcg.pocket.dex.repo.decks.FakeDecksRepo

@OptIn(ExperimentalCoroutinesApi::class)
class TierDecksViewModelTest : BehaviorSpec({
    val testDispatcher = StandardTestDispatcher()

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    Given("TierDecksViewModel with FakeDecksRepo") {
        When("데이터 로딩이 완료되면") {
            Then("Success 상태가 되어야 한다") {
                runTest(testDispatcher) {
                    val viewModel = TierDecksViewModel(decksRepo = FakeDecksRepo())
                    advanceUntilIdle()

                    viewModel.uiState.value.shouldBeInstanceOf<UiState.Success<List<DeckItemState>>>()
                }
            }
        }

        When("Success 상태에서 첫 번째 덱 아이템을 확인하면") {
            Then("expanded가 false이어야 한다") {
                runTest(testDispatcher) {
                    val viewModel = TierDecksViewModel(decksRepo = FakeDecksRepo())
                    advanceUntilIdle()

                    val state = viewModel.uiState.value as UiState.Success
                    state.data.first().expanded shouldBe false
                }
            }
        }

        When("덱을 확장하면") {
            Then("해당 덱의 expanded가 true가 되어야 한다") {
                runTest(testDispatcher) {
                    val viewModel = TierDecksViewModel(decksRepo = FakeDecksRepo())
                    advanceUntilIdle()

                    val successState = viewModel.uiState.value as UiState.Success
                    val deckItemState = successState.data.first()

                    viewModel.onExpandDeck(deckItemState)

                    val updatedState = viewModel.uiState.value as UiState.Success
                    updatedState.data.first().expanded shouldBe true
                }
            }
        }
    }

    Given("TierDecksViewModel with error-throwing DecksRepo") {
        val mockDecksRepo = mockk<DecksRepo>()

        When("DecksRepo에서 예외가 발생하면") {
            Then("Error 상태가 되어야 한다") {
                runTest(testDispatcher) {
                    coEvery { mockDecksRepo.allTierDecks() } throws RuntimeException("Network error")

                    val viewModel = TierDecksViewModel(decksRepo = mockDecksRepo)
                    advanceUntilIdle()

                    val state = viewModel.uiState.value
                    state.shouldBeInstanceOf<UiState.Error>()
                    (state as UiState.Error).message shouldBe "Network error"
                }
            }
        }
    }
})
