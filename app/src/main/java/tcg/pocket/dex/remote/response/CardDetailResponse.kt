package tcg.pocket.dex.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tcg.pocket.dex.PokemonType
import tcg.pocket.dex.Rarity
import tcg.pocket.dex.allcards.CardDetail
import tcg.pocket.dex.allcards.MoveEnergy
import tcg.pocket.dex.allcards.PokemonMove

@Serializable
data class CardDetailResponse(
    val category: String? = null,
    val id: String,
    val illustrator: String? = null,
    val image: String? = null,
    val localId: String? = null,
    val name: String,
    val rarity: String,
    val set: SetResponse? = null,
    val variants: VariantsResponse? = null,
    @SerialName("variants_detailed")
    val variantsDetailed: List<VariantsDetailedResponse>? = null,
    val dexId: List<Int>? = null,
    val hp: Int? = null,
    val types: List<String>? = null,
    val evolveFrom: String? = null,
    val description: String? = null,
    val stage: String? = null,
    val suffix: String? = null,
    val attacks: List<AttackResponse>? = null,
    val weaknesses: List<WeaknessResponse>? = null,
    val retreat: Int? = null,
    val regulationMark: String? = null,
    val legal: LegalResponse? = null,
    val updated: String? = null,
    val pricing: PricingResponse? = null,
)

@Serializable
data class SetResponse(
    val cardCount: CardCountResponse? = null,
    val id: String,
    val logo: String? = null,
    val name: String,
    val symbol: String? = null,
)

@Serializable
data class CardCountResponse(
    val official: Int? = null,
    val total: Int? = null,
)

@Serializable
data class VariantsResponse(
    val firstEdition: Boolean? = null,
    val holo: Boolean? = null,
    val normal: Boolean? = null,
    val reverse: Boolean? = null,
    val wPromo: Boolean? = null,
)

@Serializable
data class VariantsDetailedResponse(
    val type: String,
    val size: String,
)

@Serializable
data class AttackResponse(
    val cost: List<String>,
    val name: String,
    val effect: String? = null,
    val damage: String? = null,
)

@Serializable
data class WeaknessResponse(
    val type: String,
    val value: String,
)

@Serializable
data class LegalResponse(
    val standard: Boolean? = null,
    val expanded: Boolean? = null,
)

@Serializable
data class PricingResponse(
    val cardmarket: CardmarketResponse? = null,
    val tcgplayer: TcgPlayerResponse? = null,
)

@Serializable
data class CardmarketResponse(
    val updated: String? = null,
    val unit: String? = null,
    val avg: Float? = null,
    val low: Float? = null,
    val trend: Float? = null,
    val avg1: Float? = null,
    val avg7: Float? = null,
    val avg30: Float? = null,
    @SerialName("avg-holo") val avgHolo: Float? = null,
    @SerialName("low-holo") val lowHolo: Float? = null,
    @SerialName("trend-holo") val trendHolo: Float? = null,
    @SerialName("avg1-holo") val avg1Holo: Float? = null,
    @SerialName("avg7-holo") val avg7Holo: Float? = null,
    @SerialName("avg30-holo") val avg30Holo: Float? = null,
)

@Serializable
data class TcgPlayerResponse(
    val updated: String? = null,
    val unit: String? = null,
    val holofoil: HolofoilResponse? = null,
)

@Serializable
data class HolofoilResponse(
    val lowPrice: Float? = null,
    val midPrice: Float? = null,
    val highPrice: Float? = null,
    val marketPrice: Float? = null,
    val directLowPrice: Float? = null,
)

fun CardDetailResponse.toCardDetail(): CardDetail {
    val stageMap = mapOf("Stage1" to 1, "Stage2" to 2, "Basic" to 0)
    val typeString = this.types?.firstOrNull() ?: ""
    val weaknessTypeString = this.weaknesses?.firstOrNull()?.type ?: ""
    return CardDetail(
        name = this.name,
        rarity = this.rarity,
        rarityIcon = Rarity.fromString(this.rarity).icon,
        type = typeString,
        typeIcon = PokemonType.fromString(typeString).icon,
        weakness = this.weaknesses?.firstOrNull()?.value ?: "",
        weaknessType = weaknessTypeString,
        weaknessTypeIcon = PokemonType.fromString(weaknessTypeString).icon,
        hp = this.hp?.toString() ?: "",
        retreatCost = this.retreat ?: 0,
        stage = stageMap[this.stage] ?: 0,
        description = this.description ?: "",
        imageUrl = (this.image ?: "") + "/high.webp",
        pokemonMoves = this.attacks?.map { it.toPokemonMove() } ?: emptyList(),
    )
}

private fun AttackResponse.toPokemonMove(): PokemonMove {
    return PokemonMove(
        name = this.name,
        damage = this.damage ?: "",
        energy = this.cost.map { MoveEnergy(type = it, typeIcon = PokemonType.fromString(it).icon) },
        description = this.effect ?: "",
    )
}
