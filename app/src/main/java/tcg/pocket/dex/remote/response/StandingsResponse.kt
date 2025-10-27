package tcg.pocket.dex.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class StandingsResponse(
    val standings: List<StandingResponse>,
)

@Serializable
data class StandingResponse(
    val placing: Int,
    val player: PlayerResponse,
    val deck: DeckResponse? = null,
    val record: RecordResponse,
)

@Serializable
data class PlayerResponse(
    val name: String,
    val country: String? = null,
    val region: String? = null,
)

@Serializable
data class DeckResponse(
    val pokemon: List<String>? = null,
)

@Serializable
data class RecordResponse(
    val wins: Int,
    val losses: Int,
    val ties: Int,
)

data class Standing(
    val placing: Int,
    val playerName: String,
    val country: String?,
    val region: String?,
    val deckPokemon: List<String>,
    val wins: Int,
    val losses: Int,
    val ties: Int,
)

fun StandingResponse.toStanding(): Standing =
    Standing(
        placing = placing,
        playerName = player.name,
        country = player.country,
        region = player.region,
        deckPokemon = deck?.pokemon ?: emptyList(),
        wins = record.wins,
        losses = record.losses,
        ties = record.ties,
    )
