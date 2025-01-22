package tcg.pocket.dex.tierdecks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class DeckItemState(
    val content: DeckInformation,
    initialExpanded: Boolean = false,
) {
    var expanded: Boolean by mutableStateOf(initialExpanded)
        private set

    fun toggleExpanded() {
        expanded = !expanded
    }
}
