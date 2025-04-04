package tcg.pocket.dex.allcards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tcg.pocket.dex.repo.allcards.CardsRepo

class AllCardsViewModel(
    cardsRepo: CardsRepo,
) : ViewModel() {
    val cardsState: StateFlow<List<CardData>>
        field: MutableStateFlow<List<CardData>> = MutableStateFlow(cardsRepo.allCards())

    companion object {
        fun factory(cardsRepo: CardsRepo): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(AllCardsViewModel::class.java)) {
                        return AllCardsViewModel(cardsRepo = cardsRepo) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}
