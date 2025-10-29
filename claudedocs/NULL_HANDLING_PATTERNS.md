# Null Handling and Edge Case Management Patterns

## Overview
This document outlines patterns for handling null values, missing data, and edge cases found in the TcgPocketDex Android codebase.

---

## 1. Null Safety in Domain Models

### Pattern: Sealed Classes with Validation
**File**: `Card.kt`
```kotlin
sealed class Card {
    abstract val code: Code
    abstract val name: String
    abstract val rarity: Rarity
    abstract val illustrator: String
    abstract val relatedCardCodes: List<Code>
    
    data class Pokemon(
        override val code: Code,
        override val name: String,
        override val rarity: Rarity,
        override val illustrator: String,
        override val relatedCardCodes: List<Code>,
        val battleAttributes: BattleAttributes,
        val flavorText: FlavorText,
        val evolution: Evolution,
    ) : Card()
    
    data class Support(
        override val code: Code,
        override val name: String,
        override val rarity: Rarity,
        override val illustrator: String,
        override val relatedCardCodes: List<Code>,
    ) : Card()
    
    data class Item(
        override val code: Code,
        override val name: String,
        override val rarity: Rarity,
        override val illustrator: String,
        override val relatedCardCodes: List<Code>,
    ) : Card()
}
```

**Key Patterns**:
- All required fields are non-nullable in domain models
- Sealed classes ensure type safety
- Subclasses provide specific implementations

### Pattern: Evolution Validation with `require()`
**File**: `Card.kt` (lines 62-65)
```kotlin
data class Evolution(
    val stage: Int,
    val evolveFrom: List<Code> = emptyList(),
    val evolveTo: List<Code> = emptyList(),
) {
    init {
        if (stage == 0) require(evolveFrom.isEmpty()) { "Basic pokemon must not have previous evolution" }
        if (stage > 0) require(evolveFrom.isNotEmpty()) { "Evolved pokemon must have previous evolution" }
    }
}
```

**Key Patterns**:
- `require()` validates state during object construction
- Enforces business rules upfront
- Empty lists with defaults (`= emptyList()`) for optional collections

### Pattern: Deck Composition Validation
**File**: `CardDeck.kt`
```kotlin
data class CardDeck(
    val name: String,
    val keyCards: List<Card>,
    val allCards: List<Card>,
) {
    init {
        require(allCards.containsAll(keyCards)) { 
            "key cards in this deck aren't contained in allCards: keyCards - $keyCards" 
        }
    }
}
```

**Key Patterns**:
- Validates relationships between collections
- Error messages include actual data for debugging
- Uses `containsAll()` for safe list comparison

### Pattern: Default Values for Optional Fields
**File**: `Card.kt` (lines 31-32)
```kotlin
data class BattleAttributes(
    val type: PokemonType,
    val hp: Int,
    val moves: List<Move>,
    val ability: Ability = Ability.NONE,  // Safe default
    val weakness: PokemonType,
    val retreatCost: Int,
)
```

**Key Patterns**:
- Use constant singleton defaults (`Ability.NONE`)
- Prevents null checks downstream
- Default companion object provides safety net

---

## 2. Null Handling in API Response Mapping

### Pattern: Nullable Response Fields with Safe Defaults
**File**: `CardDetailResponse.kt`
```kotlin
@Serializable
data class CardDetailResponse(
    val category: String = "",
    val id: String,
    val illustrator: String? = null,        // Optional
    val image: String? = null,               // Optional
    val localId: String? = null,             // Optional
    val name: String,
    val rarity: String,
    val set: SetResponse? = null,            // Optional
    val dexId: List<Int>? = null,           // Optional
    val hp: Int? = null,                     // Optional
    val types: List<String>? = null,        // Optional
    val attacks: List<AttackResponse>? = null,  // Optional
    val weaknesses: List<WeaknessResponse>? = null,  // Optional
    // ... more fields
)
```

**Key Patterns**:
- Only critical fields are non-nullable
- All optional API responses have `= null` defaults
- Type of nullability explicitly declared

### Pattern: Safe Mapping with `?.` and `?:`
**File**: `CardDetailResponse.kt` (lines 132-155)
```kotlin
fun CardDetailResponse.toCardDetail(): CardDetail {
    val stageMap = mapOf("Stage1" to 1, "Stage2" to 2, "Basic" to 0)
    val typeString = this.types?.firstOrNull() ?: ""              // Safe extraction
    val weaknessTypeString = this.weaknesses?.firstOrNull()?.type ?: ""  // Chained safe access
    
    return CardDetail(
        category = this.category,
        name = this.name,
        rarity = this.rarity,
        rarityIcon = Rarity.fromString(this.rarity).icon,
        type = typeString,
        typeIcon = PokemonType.fromString(typeString).icon,        // Handles empty string
        weakness = this.weaknesses?.firstOrNull()?.value ?: "",    // Safe with default
        weaknessType = weaknessTypeString,
        weaknessTypeIcon = PokemonType.fromString(weaknessTypeString).icon,
        hp = this.hp?.toString() ?: "",                            // Null to empty string
        retreatCost = this.retreat ?: 0,                           // Null to zero
        stage = stageMap[this.stage] ?: 0,                         // Map with default
        description = this.description ?: "",                      // Null to empty
        imageUrl = (this.image ?: "") + "/high.webp",             // Safe concatenation
        pokemonMoves = this.attacks?.map { it.toPokemonMove() } ?: emptyList(),  // Collection safety
        trainerType = trainerType,
        effect = this.effect,
    )
}

private fun AttackResponse.toPokemonMove(): PokemonMove {
    return PokemonMove(
        name = this.name,
        damage = this.damage ?: "",               // Null coalescing
        energy = this.cost.map { MoveEnergy(...) },  // No null check needed
        description = this.effect ?: "",          // Null coalescing
    )
}
```

**Key Patterns**:
- `?.firstOrNull()` for safe list access
- `?:` operator for null coalescing
- Chained safe access for nested properties
- Empty strings as defaults for nullable strings
- Empty lists for nullable collections
- Default numeric values (0) for nullable numbers

### Pattern: Nullable Deck Handling in Tournament Data
**File**: `StandingsResponse.kt` (lines 48-58)
```kotlin
@Serializable
data class StandingResponse(
    val placing: Int,
    val player: PlayerResponse,
    val deck: DeckResponse? = null,           // Deck optional
    val record: RecordResponse,
)

@Serializable
data class DeckResponse(
    val pokemon: List<String>? = null,        // Pokemon list optional
)

data class Standing(
    val placing: Int,
    val playerName: String,
    val country: String?,                     // Nullable in domain
    val region: String?,                      // Nullable in domain
    val deckPokemon: List<String>,            // Non-null in domain
    val wins: Int,
    val losses: Int,
    val ties: Int,
)

fun StandingResponse.toStanding(): Standing =
    Standing(
        placing = placing,
        playerName = player.name,
        country = player.country,              // Preserved as nullable
        region = player.region,                // Preserved as nullable
        deckPokemon = deck?.pokemon ?: emptyList(),  // Safe extraction with default
        wins = record.wins,
        losses = record.losses,
        ties = record.ties,
    )
```

**Key Patterns**:
- Nullable fields in response DTOs
- Non-null collections in domain models (never null)
- `deck?.pokemon ?: emptyList()` pattern for nested optionals
- Preserves nullable domain fields when semantically appropriate

---

## 3. Empty Collection Handling

### Pattern: isEmpty() Checks Before Rendering
**File**: `PokemonMovesSection.kt` (lines 26-34)
```kotlin
@Composable
fun PokemonMovesSection(
    pokemonMoves: List<PokemonMove>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        if (pokemonMoves.isEmpty()) {
            return@Column  // Early return for empty state
        }
        PocketDexSectionHeader(text = "Moves")
        
        Surface {
            Column {
                pokemonMoves.forEach { move ->
                    // Render each move
                }
            }
        }
    }
}
```

**Key Patterns**:
- Explicit empty check with `isEmpty()`
- Early return to avoid rendering empty UI
- Prevents null pointer exceptions in iteration

### Pattern: Collection Defaults with `emptyList()`
**File**: `Card.kt` (line 59-60)
```kotlin
data class Evolution(
    val stage: Int,
    val evolveFrom: List<Code> = emptyList(),    // Default empty
    val evolveTo: List<Code> = emptyList(),      // Default empty
)
```

**Key Patterns**:
- Never use `null` for optional collections
- Always provide `emptyList()` default
- Consumers never check for null

### Pattern: hasDescription Computed Property
**File**: `PokemonMovesSection.kt` (lines 93-101)
```kotlin
data class PokemonMove(
    val name: String,
    val damage: String,
    val energy: List<MoveEnergy>,
    val description: String,
) {
    val hasDescription: Boolean
        get() = description.isNotEmpty()  // Safe check
}

// Usage in UI
if (move.hasDescription) {
    Text(text = move.description, ...)
}
```

**Key Patterns**:
- Computed property for state detection
- `isNotEmpty()` for string validation
- Declarative instead of imperative checks

---

## 4. String Handling and Empty String Defaults

### Pattern: Empty String as Null Replacement
**File**: `CardDetailResponse.kt` (line 150)
```kotlin
imageUrl = (this.image ?: "") + "/high.webp",  // Null to empty, then concatenate
```

**Key Patterns**:
- Null strings become empty strings
- Safe for string concatenation
- Prevents "null" string literals

### Pattern: Rarity Mapping with Unknown Default
**File**: `Rarity.kt` (lines 54-68)
```kotlin
companion object {
    fun fromString(rarity: String): Rarity {
        return when (rarity) {
            "Common", "One Diamond" -> Common
            "Uncommon", "Two Diamond" -> Uncommon
            "Rare", "Three Diamond" -> Rare
            "Double Rare", "Ultra Rare", "Four Diamond" -> DoubleRare
            "Illustration Rare", "Art Rare", "One Star" -> IllustrationRare
            "Super Rare", "Special Illustration Rare", "Special Art Rare", "Two Star" -> SuperRare
            "Immersive Rare", "Three Star" -> ImmersiveRare
            "Crown Rare", "Crown" -> CrownRare
            "Shiny Rare", "One Shiny" -> ShinyRare
            "Double Shiny Rare", "Two Shiny" -> DoubleShinyRare
            else -> Unknown  // Default for unrecognized values
        }
    }
}
```

**Key Patterns**:
- When expressions with comprehensive mapping
- `else -> Unknown` fallback prevents exceptions
- Safe parsing of string enums

---

## 5. Error Handling in Data Source Layer

### Pattern: Try-Catch with Empty List Fallback
**File**: `RemoteTournamentDataSource.kt` (lines 26-47)
```kotlin
suspend fun getTop8Standings(tournamentIds: List<String>): List<Standing> =
    coroutineScope {
        tournamentIds
            .chunked(5)
            .flatMap { chunk ->
                val results =
                    chunk.map { id ->
                        async {
                            try {
                                tournamentStatsService.fetchStandings(id).standings
                                    .filter { it.placing <= 8 }
                            } catch (e: Exception) {
                                println("Failed to fetch standings for tournament $id: ${e.message}")
                                emptyList()  // Graceful degradation
                            }
                        }
                    }
                delay(200)
                results.awaitAll().flatten()
            }
            .map { it.toStanding() }
    }
```

**Key Patterns**:
- Try-catch returns empty list on error
- Logging includes context (tournament ID, error message)
- Doesn't throw, allows partial success
- Graceful degradation pattern

### Pattern: Batch Processing with Error Isolation
**File**: `RemoteTournamentDataSource.kt` (lines 28-44)
```kotlin
tournamentIds
    .chunked(5)              // Batch into groups of 5
    .flatMap { chunk ->
        val results = chunk.map { id ->
            async {
                try {
                    // Fetch for single tournament
                } catch (e: Exception) {
                    emptyList()  // Individual failure doesn't break batch
                }
            }
        }
        delay(200)  // Rate limiting between batches
        results.awaitAll().flatten()
    }
```

**Key Patterns**:
- Chunking prevents cascading failures
- Each async task isolated
- Delay for API rate limiting
- Flattening combines batch results

---

## 6. Boundary Testing and Edge Cases

### Pattern: Comprehensive Edge Case Testing
**File**: `RemoteTournamentDataSourceTest.kt`

**Test Cases Include**:

1. **Boundary Value Testing** (lines 24-48)
   - Exactly at boundary (32 players)
   - Just above boundary (33 players)
   - Just below boundary (31 players)

2. **Empty Collection Testing** (lines 145-159)
   - Empty tournament list returns empty result

3. **Large Data Testing** (lines 349-375)
   - 1000 tournaments with modulo pattern
   - Tests performance and correctness at scale

4. **Null Field Testing** (lines 397-413)
   - Null optional fields don't break filtering
   - Date field nullability tested

5. **Extreme Values** (lines 184-202)
   - Zero players
   - 10,000 players
   - Single vs bulk operations

**Key Pattern**:
```kotlin
When("date 필드가 null인 토너먼트가 있을 때") {
    Then("필터링 로직에 영향을 주지 않아야 한다") {
        coEvery { mockService.fetchTournaments("POCKET") } returns
            listOf(
                TournamentResponse("t1", "Event with date", "2025-01-01", "POCKET", 40),
                TournamentResponse("t2", "Event without date", null, "POCKET", 50),
            )
        
        val result = dataSource.getQualifyingTournamentIds()
        
        result shouldHaveSize 2
        result.map { it.id } shouldContainExactly listOf("t1", "t2")
    }
}
```

---

## 7. Test Data and Validation

### Pattern: Safe Test Data with Defaults
**File**: `FakeCard.kt` (lines 38-45)
```kotlin
val fakePikachuCard: Card.Pokemon by lazy {
    Card.Pokemon(
        code = fakePikachuCardCode,
        name = "pikachu",
        rarity = Rarity.Common,
        illustrator = "sh1mj1",
        relatedCardCodes = listOf(fakeRaichuCardCode),
        battleAttributes = Card.Pokemon.BattleAttributes(
            type = PokemonType.Electric,
            hp = 60,
            moves = listOf(/* ... */),
            ability = Card.Pokemon.BattleAttributes.Ability.NONE,  // Safe default
            weakness = PokemonType.Fighting,
            retreatCost = 1,
        ),
        flavorText = Card.Pokemon.FlavorText(
            dexId = 25,
            species = "Mouse",
            height = 0.4f,
            weight = 6.0f,
            description = "When it is angered...",
        ),
        evolution = Card.Pokemon.Evolution(
            stage = 0,
            evolveFrom = emptyList(),  // Valid for stage 0
            evolveTo = listOf(fakeRaichuCardCode),
        ),
    )
}
```

**Key Patterns**:
- Lazy initialization prevents repeated creation
- All required fields provided
- Follows domain validation rules
- Test data respects business constraints

### Pattern: Evolution Validation Tests
**File**: `EvolutionTest.kt`
```kotlin
Given("A pokemon's evolution stage is 0") {
    When("the evolutionFrom is not empty") {
        Then("throw IllegalArgumentException") {
            shouldThrow<IllegalArgumentException> {
                Card.Pokemon.Evolution(
                    stage = 0,
                    evolveFrom = listOf(fakePikachuCardCode),  // Invalid!
                    evolveTo = emptyList(),
                )
            }
        }
    }
}

Given("A pokemon's evolution stage is greater than 0") {
    When("the evolutionFrom is empty") {
        Then("throw IllegalArgumentException") {
            shouldThrow<IllegalArgumentException> {
                Card.Pokemon.Evolution(
                    stage = 1,
                    evolveFrom = emptyList(),  // Invalid!
                    evolveTo = emptyList(),
                )
            }
        }
    }
}
```

**Key Patterns**:
- Test validates business rule enforcement
- Uses `shouldThrow` and `shouldNotThrow`
- Tests both valid and invalid state combinations

### Pattern: Nullable Field Mapping Tests
**File**: `StandingsResponseTest.kt` (lines 42-71)
```kotlin
test("toStanding should handle nullable fields correctly when all are null") {
    val response = StandingResponse(
        placing = 5,
        player = PlayerResponse(
            name = "Anonymous Player",
            country = null,      // Null field
            region = null,       // Null field
        ),
        deck = null,             // Null deck
        record = RecordResponse(wins = 5, losses = 3, ties = 0),
    )
    
    val result = response.toStanding()
    
    result.placing shouldBe 5
    result.playerName shouldBe "Anonymous Player"
    result.country shouldBe null        // Preserved
    result.region shouldBe null         // Preserved
    result.deckPokemon shouldBe emptyList()  // Mapped to empty list
    result.wins shouldBe 5
    result.losses shouldBe 3
    result.ties shouldBe 0
}
```

**Key Patterns**:
- Tests all possible null combinations
- Validates nullable preservation
- Tests default list generation

---

## 8. Safe Collection Operations

### Pattern: Safe List Operations with Defaults
**File**: `CardDetailResponse.kt` (line 151)
```kotlin
pokemonMoves = this.attacks?.map { it.toPokemonMove() } ?: emptyList()
```

**Key Patterns**:
- `?.map()` for safe transformation
- `?:` provides empty list fallback
- Never passes null to domain layer

### Pattern: Safe First/Last Access
**File**: `CardDetailResponse.kt` (lines 134-135)
```kotlin
val typeString = this.types?.firstOrNull() ?: ""
val weaknessTypeString = this.weaknesses?.firstOrNull()?.type ?: ""
```

**Key Patterns**:
- `?.firstOrNull()` prevents exceptions
- Chained safe access for nested objects
- Default empty string for missing data

### Pattern: Safe Contains Check
**File**: `CardDeck.kt`
```kotlin
require(allCards.containsAll(keyCards)) { "..." }
```

**Key Patterns**:
- `containsAll()` for safe subset checking
- Error message includes context
- Validation at construction

---

## 9. UI-Level Null Handling

### Pattern: Early Return for Empty State
**File**: `PokemonMovesSection.kt` (lines 31-34)
```kotlin
if (pokemonMoves.isEmpty()) {
    return@Column  // Skip rendering
}
```

**Key Patterns**:
- Explicit empty state handling
- Prevents rendering empty sections
- Early return pattern in Compose

### Pattern: Computed Properties for State
**File**: `PokemonMovesSection.kt` (lines 99-101)
```kotlin
data class PokemonMove(
    val name: String,
    val damage: String,
    val energy: List<MoveEnergy>,
    val description: String,
) {
    val hasDescription: Boolean
        get() = description.isNotEmpty()
}
```

**Key Patterns**:
- Encapsulate state checks in properties
- Avoid scattered null checks in UI
- Boolean properties for conditional rendering

---

## Summary of Patterns

| Pattern | Use Case | Example |
|---------|----------|---------|
| `require()` in init | Validate state during construction | Evolution validation |
| `?.firstOrNull() ?: default` | Safe list access with fallback | Type extraction from API response |
| `emptyList()` default | Never pass null collections | API response mapping |
| `isEmpty()` check | Guard conditional rendering | UI section rendering |
| Try-catch with fallback | Handle API errors gracefully | Network data source |
| `when(value) { else -> Unknown }` | Safe enum parsing | Rarity mapping |
| Computed properties | Encapsulate state checks | hasDescription property |
| Chunked processing | Batch operations with isolation | Tournament standings fetch |
| Lazy initialization | Defer expensive creation | Test data setup |

---

## Best Practices Summary

1. **Domain Models**: Always non-nullable, with validation in `init`
2. **Response DTOs**: Mark all optional fields with `? = null`
3. **Mapping**: Use `?.` and `?:` for safe null handling
4. **Collections**: Use `emptyList()` never `null`
5. **Defaults**: Provide sensible defaults (empty string, zero, Unknown enum)
6. **UI**: Check `isEmpty()` before rendering, not null
7. **Errors**: Catch and fallback to empty collections gracefully
8. **Testing**: Comprehensive edge case coverage including null scenarios
9. **Validation**: Enforce rules at construction with `require()`
10. **Properties**: Use computed properties to encapsulate state checks
