package tcg.pocket.dex.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class TournamentResponse(
    val id: String,
    val name: String,
    val date: String? = null,
    val game: String,
    val players: Int,
)

data class TournamentId(val id: String)

fun TournamentResponse.toTournamentId(): TournamentId = TournamentId(id = id)
