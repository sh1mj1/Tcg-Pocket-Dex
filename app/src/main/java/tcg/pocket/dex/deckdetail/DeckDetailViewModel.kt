package tcg.pocket.dex.deckdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DeckDetailViewModel(
    val deckId: String,
) : ViewModel() {
    companion object {
        fun factory(deckId: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    require(modelClass.isAssignableFrom(DeckDetailViewModel::class.java)) {
                        "Unknown ViewModel class"
                    }
                    return DeckDetailViewModel(deckId) as T
                }
            }
    }
}
