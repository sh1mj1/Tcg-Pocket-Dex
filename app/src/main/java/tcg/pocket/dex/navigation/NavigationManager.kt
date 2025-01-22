package tcg.pocket.dex.navigation

import androidx.compose.runtime.mutableStateListOf

class NavigationManager {
    private val _backstack = mutableStateListOf<Screen>(Screen.TierDecks)
    val backstack: List<Screen> get() = _backstack

    val currentScreen: Screen
        get() = _backstack.last()

    fun navigateTo(screen: Screen) {
        _backstack.add(screen)
    }

    fun navigateBack() {
        if (backstackIsEmpty()) {
            _backstack.removeAt(_backstack.lastIndex)
        }
    }

    private fun backstackIsEmpty() = backstack.size > 1
}
