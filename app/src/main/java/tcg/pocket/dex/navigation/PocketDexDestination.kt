package tcg.pocket.dex.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument

// TODO: create object lazily?
interface PocketDexDestination {
    val route: String
}

interface BottomNavDestination : PocketDexDestination {
    val icon: ImageVector
    val text: String
}

object AllCards : BottomNavDestination {
    override val icon = Icons.AutoMirrored.Filled.List
    override val route = "all_cards"
    override val text: String = "All Cards"
}

object TierDecks : BottomNavDestination {
    override val icon = Icons.Default.Star
    override val route = "tier_decks"
    override val text: String = "Tier Decks"
}

object ExpansionPacks : BottomNavDestination {
    override val icon = Icons.Default.AccountBox
    override val route = "expansion_packs"
    override val text: String = "Expansion Packs"
}

object TierDeckDetail : PocketDexDestination {
    override val route: String = "tier_deck_detail"
    const val DECK_ID_ARG = "deck_id"
    val routeWithArgs = "$route/{$DECK_ID_ARG}"

    val arguments =
        listOf(
            navArgument(DECK_ID_ARG) { type = NavType.StringType },
        )

    fun routeWithArgs(deckId: String): String = "$route/$deckId"
}

object Search : PocketDexDestination {
    override val route: String = "search"
    const val SEARCH_TYPE_ARG = "search_type"
    val routeWithArgs = "$route/{$SEARCH_TYPE_ARG}"

    val arguments =
        listOf(
            navArgument(SEARCH_TYPE_ARG) { type = NavType.StringType },
        )

    fun routeWithArgs(searchType: String): String = "$route/$searchType"
}

val bottomBarScreens = listOf(AllCards, TierDecks, ExpansionPacks)
