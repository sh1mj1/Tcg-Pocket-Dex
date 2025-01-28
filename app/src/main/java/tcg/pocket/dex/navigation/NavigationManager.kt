package tcg.pocket.dex.navigation

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NavigationManager(
    initialBackstack: List<Screen> = listOf(Screen.BottomNavigation.TierDecks),
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
        check(navigateBackAvailable()) { "Cannot navigate back from the root screen" }
        _backstack.removeAt(_backstack.lastIndex)
    }

    fun navigateBackAvailable(): Boolean = backstack.size > 1

    companion object {
        fun factory(initialBackstack: List<Screen> = listOf(Screen.BottomNavigation.TierDecks)): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(NavigationManager::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return NavigationManager(initialBackstack) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}
