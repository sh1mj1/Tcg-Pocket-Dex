package tcg.pocket.dex.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class BriefCardsResponse(
    val cards: List<BriefCard>,
)

@Serializable
data class BriefCard(
    val id: String,
    val image: String? = null,
    val localId: String,
    val name: String,
)
