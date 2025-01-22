package tcg.pocket.dex.navigation

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel

class NavigationManager(
    initialBackstack: List<Screen> = listOf(Screen.TierDecks),
) : ViewModel() {
    init {
        require(initialBackstack.isNotEmpty()) { "Initial backstack must not be empty" }
    }

    private val _backstack = initialBackstack.toMutableStateList()
    val backstack: List<Screen> get() = _backstack

    val currentScreen: Screen
        get() = _backstack.last()

    fun navigateTo(screen: Screen) {
        _backstack.add(screen)
    }

    fun navigateBack() {
        check(backstackIsEmpty()) { "Cannot navigate back from the first screen" }
        if (backstackIsEmpty()) {
            _backstack.removeAt(_backstack.lastIndex)
        }
    }

    private fun backstackIsEmpty() = backstack.size > 1
}
