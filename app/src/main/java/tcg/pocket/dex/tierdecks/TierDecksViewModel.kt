package tcg.pocket.dex.tierdecks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tcg.pocket.dex.repo.decks.DecksRepo

class TierDecksViewModel(
    decksRepo: DecksRepo,
) : ViewModel() {
    val deckItemsState: StateFlow<List<DeckItemState>>
        field: MutableStateFlow<List<DeckItemState>> =
        MutableStateFlow(
            decksRepo.allTierDecks().map(::DeckItemState),
        )

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

    companion object {
        fun factory(decksRepo: DecksRepo): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(TierDecksViewModel::class.java)) {
                        return TierDecksViewModel(decksRepo) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}
