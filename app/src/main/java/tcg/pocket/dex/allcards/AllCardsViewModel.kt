package tcg.pocket.dex.allcards

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tcg.pocket.dex.tierdecks.fakeCardsData

class AllCardsViewModel(
    cards: List<CardData> = fakeCardsData,
) : ViewModel() {
    val cardsState: StateFlow<List<CardData>>
        field: MutableStateFlow<List<CardData>> = MutableStateFlow(cards)
}
