package tcg.pocket.dex.tierdecks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tcg.pocket.dex.common.UiState
import tcg.pocket.dex.repo.decks.DecksRepo
import timber.log.Timber

class TierDecksViewModel(
    private val decksRepo: DecksRepo,
) : ViewModel() {
    val uiState: StateFlow<UiState<List<DeckItemState>>>
        field = MutableStateFlow<UiState<List<DeckItemState>>>(UiState.Loading)

    init {
        viewModelScope.launch {
            try {
                val decks = decksRepo.allTierDecks()
                Timber.d("Loaded ${decks.size} decks")

                if (decks.isEmpty()) {
                    uiState.value = UiState.Error("데이터를 찾을 수 없습니다")
                } else {
                    uiState.value = UiState.Success(decks.map(::DeckItemState))
                }
            } catch (e: Exception) {
                Timber.e(e, "Error loading decks")
                uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun onExpandDeck(deckItemState: DeckItemState) {
        val currentState = uiState.value
        if (currentState is UiState.Success) {
            uiState.update { _ ->
                UiState.Success(
                    currentState.data.map { state ->
                        if (state == deckItemState) {
                            state.expansionToggled()
                        } else {
                            state
                        }
                    },
                )
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
