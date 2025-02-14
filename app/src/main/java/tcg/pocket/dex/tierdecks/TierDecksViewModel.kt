package tcg.pocket.dex.tierdecks

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TierDecksViewModel(
    decksInformation: List<DeckInformation> = fakeDecksInformation,
) : ViewModel() {
    val deckItemsState: StateFlow<List<DeckItemState>>
        field = MutableStateFlow(decksInformation.map(::DeckItemState))

    fun onExpandDeck(
        deckItemState: DeckItemState,
        expanded: Boolean,
    ) {
        // TODO: DeckItemState data class?
        deckItemsState.value.forEach {
            if (it == deckItemState) it.toggleExpanded()
        }
    }
}
