package tcg.pocket.dex.tierdecks

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class TierDecksViewModelTest : FunSpec({
    val viewModel =
        TierDecksViewModel(
            fakeDecksInformation,
        )

    test("the deckItems are not expanded at the beginning") {
        viewModel.deckItemsState.value.first().expanded shouldBe false
    }

    test("expand the deck") {
        // given
        val deckItemState = viewModel.deckItemsState.value.first()
        val expanded = true

        // when
        viewModel.onExpandDeck(deckItemState, expanded)

        // then
        viewModel.deckItemsState.value.first().expanded shouldBe true
    }
})
