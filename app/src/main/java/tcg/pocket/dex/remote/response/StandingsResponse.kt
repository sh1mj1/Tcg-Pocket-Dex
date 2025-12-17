package tcg.pocket.dex.remote.response

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
data class StandingsResponse(
    val standings: List<StandingResponse>,
)

@Serializable
data class StandingResponse(
    val placing: Int?,
    val player: PlayerResponse,
    val deck: DeckResponse? = null,
    val record: RecordResponse,
)

@Serializable(with = PlayerResponseSerializer::class)
data class PlayerResponse(
    val name: String,
    val country: String? = null,
    val region: String? = null,
)

object PlayerResponseSerializer : KSerializer<PlayerResponse> {
    override val descriptor = buildClassSerialDescriptor("PlayerResponse")

    override fun deserialize(decoder: Decoder): PlayerResponse {
        val element = decoder.decodeSerializableValue(JsonElement.serializer())
        return when (element) {
            is JsonPrimitive -> {
                // Handle string format: "player":"username"
                PlayerResponse(name = element.content)
            }
            is JsonObject -> {
                // Handle object format: "player":{"name":"...","country":"..."}
                val name = element["name"]?.let { (it as? JsonPrimitive)?.content } ?: ""
                val country =
                    when (val countryElement = element["country"]) {
                        null, is JsonNull -> null
                        is JsonPrimitive -> countryElement.content
                        else -> null
                    }
                val region =
                    when (val regionElement = element["region"]) {
                        null, is JsonNull -> null
                        is JsonPrimitive -> regionElement.content
                        else -> null
                    }
                PlayerResponse(name = name, country = country, region = region)
            }
            else -> throw SerializationException("Unexpected player format: ${element::class}")
        }
    }

    override fun serialize(
        encoder: Encoder,
        value: PlayerResponse,
    ) {
        val jsonObject =
            buildJsonObject {
                put("name", value.name)
                value.country?.let { put("country", it) }
                value.region?.let { put("region", it) }
            }
        encoder.encodeSerializableValue(JsonObject.serializer(), jsonObject)
    }
}

@Serializable
data class DeckResponse(
    val id: String? = null,
    val name: String? = null,
    val icons: List<String>? = null,
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
        placing = placing ?: 0,
        playerName = player.name,
        country = player.country,
        region = player.region,
        deckPokemon = deck?.icons ?: emptyList(),
        wins = record.wins,
        losses = record.losses,
        ties = record.ties,
    )
