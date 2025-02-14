package tcg.pocket.dex.tierdecks

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TierDecksViewModel(
    decksInformation: List<DeckInformation> = fakeDecksInformation,
) : ViewModel() {
    val deckItemsState: StateFlow<List<DeckItemState>>
        field: MutableStateFlow<List<DeckItemState>> = MutableStateFlow(decksInformation.map(::DeckItemState))

    fun onExpandDeck(deckItemState: DeckItemState) {
        deckItemsState.value =
            deckItemsState.value.map { state ->
                if (state == deckItemState) {
                    state.expansionToggled()
                } else {
                    state
                }
            }
    }
}
