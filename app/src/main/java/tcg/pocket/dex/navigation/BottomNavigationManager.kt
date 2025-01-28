package tcg.pocket.dex.navigation

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BottomNavigationManager(
    initialBackstack: List<Screen.BottomNavigation> = listOf(Screen.BottomNavigation.TierDecks),
    val onBackStackIsEmpty: () -> Unit,
) : ViewModel() {
    init {
        require(initialBackstack.isNotEmpty()) { "Initial backstack must not be empty" }
    }

    private val _backstack = initialBackstack.toMutableStateList()
    val backstack: List<Screen> get() = _backstack

    val currentScreen: Screen.BottomNavigation
        get() = _backstack.last()

    fun navigateTo(screen: Screen.BottomNavigation) {
        _backstack.add(screen)
    }

    fun navigateBack() {
        check(navigateBackAvailable()) { "Cannot navigate back from the root screen" }
        _backstack.removeAt(_backstack.lastIndex)
    }

    fun navigateBackAvailable(): Boolean = backstack.size > 1

    companion object {
        fun factory(
            initialBackstack: List<Screen.BottomNavigation> = listOf(Screen.BottomNavigation.TierDecks),
            onBackStackIsEmpty: () -> Unit,
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(BottomNavigationManager::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return BottomNavigationManager(initialBackstack, onBackStackIsEmpty) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}
