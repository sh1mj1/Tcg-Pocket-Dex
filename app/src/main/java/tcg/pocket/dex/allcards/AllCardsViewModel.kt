package tcg.pocket.dex.allcards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// TODO: user repository pattern
class AllCardsViewModel(
    cards: List<CardData>,
) : ViewModel() {
    val cardsState: StateFlow<List<CardData>>
        field: MutableStateFlow<List<CardData>> = MutableStateFlow(cards)

    companion object {
        fun factory(cards: List<CardData>): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(AllCardsViewModel::class.java)) {
                        return AllCardsViewModel(cards) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}
