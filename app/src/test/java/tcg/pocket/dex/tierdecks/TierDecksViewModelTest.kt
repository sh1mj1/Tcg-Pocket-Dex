package tcg.pocket.dex.tierdecks

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import tcg.pocket.dex.repo.decks.FakeDecksRepo

class TierDecksViewModelTest : FunSpec({
    val viewModel =
        TierDecksViewModel(
            decksRepo = FakeDecksRepo(),
        )

    test("the deckItems are not expanded at the beginning") {
        viewModel.deckItemsState.value.first().expanded shouldBe false
    }

    test("expand the deck") {
        // given
        val deckItemState = viewModel.deckItemsState.value.first()

        // when
        viewModel.onExpandDeck(deckItemState)

        // then
        viewModel.deckItemsState.value.first().expanded shouldBe true
    }
})
