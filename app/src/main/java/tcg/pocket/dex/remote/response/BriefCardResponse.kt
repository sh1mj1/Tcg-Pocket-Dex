package tcg.pocket.dex.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class BriefCardResponse(
    val id: String,
    val localId: String,
    val name: String,
    val image: String? = null,
)
