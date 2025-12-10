package tcg.pocket.dex.tierdecks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tcg.pocket.dex.repo.decks.DecksRepo

class TierDecksViewModel(
    private val decksRepo: DecksRepo,
) : ViewModel() {
    private val _deckItemsState = MutableStateFlow<List<DeckItemState>>(emptyList())
    val deckItemsState: StateFlow<List<DeckItemState>> = _deckItemsState

    init {
        viewModelScope.launch {
            val decks = decksRepo.allTierDecks()
            _deckItemsState.value = decks.map(::DeckItemState)
        }
    }

    fun onExpandDeck(deckItemState: DeckItemState) {
        _deckItemsState.value =
            _deckItemsState.value.map { state ->
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
