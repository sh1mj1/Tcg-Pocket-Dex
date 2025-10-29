# Repository and Data Source Patterns Analysis
## TcgPocketDex Project

### Executive Summary
The TcgPocketDex project follows a **clean architecture pattern** with clear separation between UI, Repository, Data Source, and Network layers. The codebase uses **manual dependency injection** with factory functions in ViewModels. Understanding these patterns is critical for implementing consistent deck statistics aggregation features.

---

## 1. ARCHITECTURE LAYERS

### 1.1 Layer Hierarchy
```
UI Layer (Composables/ViewModels)
    ↓
Repository Layer (Interfaces + Implementations)
    ↓
Data Source Layer (Remote/Local data access)
    ↓
Network Layer (HTTP services)
    ↓
Remote API
```

### 1.2 Layer Responsibilities

**UI Layer**: 
- Jetpack Compose screens and ViewModels
- State management
- Navigation handling
- User interactions

**Repository Layer**:
- Abstract data access behind interfaces
- Coordinate between multiple data sources
- Data transformation/mapping
- Single source of truth for app data

**Data Source Layer**:
- Fetch data from specific sources (Remote, Local, Cache)
- Response DTO to domain model conversion
- Source-specific logic (chunking, parallel fetching, error handling)

**Network Layer**:
- Ktor HTTP client services
- API endpoint definitions
- Request/response serialization

---

## 2. EXISTING REPOSITORY PATTERNS

### 2.1 CardsRepo Pattern

**Interface** (`CardsRepo.kt`):
```kotlin
interface CardsRepo {
    suspend fun allCards(): List<CardData>
    suspend fun cardDetail(id: String): CardDetail
    fun relatedCards(id: String): List<CardData>
}
```

**Key Characteristics**:
- Mix of suspend (async) and non-suspend (sync) functions
- Returns domain models (CardData, CardDetail), not DTOs
- Methods focused on single responsibilities

**Implementation** (`DefaultCardsRepo.kt`):
```kotlin
class DefaultCardsRepo(
    private val remoteCardsDataSource: RemoteCardsDataSource,
) : CardsRepo {
    override suspend fun allCards(): List<CardData> = 
        remoteCardsDataSource.allCards()
    
    override suspend fun cardDetail(id: String): CardDetail = 
        remoteCardsDataSource.cardDetail(id)
    
    override fun relatedCards(id: String): List<CardData> = 
        fakeRelatedCards // TODO: modify
}
```

**Key Characteristics**:
- Single dependency injection via constructor
- Simple delegation to data source (no additional logic yet)
- Some data sources still return fake data (relatedCards)

### 2.2 DecksRepo Pattern

**Interface** (`DecksRepo.kt`):
```kotlin
interface DecksRepo {
    fun allTierDecks(): List<DeckInformation>
}
```

**Implementation** (`FakeDecksRepo.kt` - no real implementation yet):
```kotlin
class FakeDecksRepo(
    private val tierDecks: List<DeckInformation> = fakeTierDecksInformation,
) : DecksRepo {
    override fun allTierDecks(): List<DeckInformation> = tierDecks
}
```

**Key Characteristics**:
- Currently only has fake implementation (no real data source)
- Non-suspend function (sync)
- Used for preview/testing

---

## 3. DATA SOURCE PATTERNS

### 3.1 CardsDataSource Pattern

**Interface** (`CardsDataSource.kt`):
```kotlin
interface CardsDataSource {
    suspend fun allCards(): List<CardData>
    suspend fun cardDetail(id: String): CardDetail
    fun relatedCards(id: String): List<CardData>
}
```

**Implementation** (`RemoteCardsDataSource.kt`):
```kotlin
class RemoteCardsDataSource(
    private val cardsService: CardsService,
) : CardsDataSource {
    private val setIds = listOf("P-A", "A1", "A1a", "A2", "A2a", "A2b", "A3", "A3a", "A3b", "A4", "A4a")
    
    override suspend fun allCards(): List<CardData> =
        coroutineScope {
            val deferredCards = setIds.map { setId ->
                async { cardsService.briefCards(setId).cards }
            }
            deferredCards.awaitAll().flatten().toCardDataList()
        }
    
    override suspend fun cardDetail(id: String): CardDetail =
        cardsService.cardDetail(id).toCardDetail()
}
```

**Key Characteristics**:
- Uses coroutineScope for parallel fetching
- async/awaitAll for concurrent API calls
- Internal helper functions for DTO conversion (toCardDataList())
- Mappers follow pattern: `BriefCard.toCardData()`, `CardsService.toCardDetail()`

### 3.2 RemoteTournamentDataSource Pattern (NEW)

**Implementation** (`RemoteTournamentDataSource.kt`):
```kotlin
class RemoteTournamentDataSource(
    private val tournamentStatsService: TournamentStatsService,
) {
    suspend fun getQualifyingTournamentIds(
        game: String = "POCKET",
        minPlayers: Int = 32,
    ): List<TournamentId> {
        return tournamentStatsService
            .fetchTournaments(game)
            .filter { it.players > minPlayers }
            .map { it.toTournamentId() }
    }
    
    suspend fun getTop8Standings(tournamentIds: List<String>): List<Standing> =
        coroutineScope {
            tournamentIds
                .chunked(5)  // Rate limiting strategy
                .flatMap { chunk ->
                    val results = chunk.map { id ->
                        async {
                            try {
                                tournamentStatsService.fetchStandings(id).standings
                                    .filter { it.placing <= 8 }
                            } catch (e: Exception) {
                                println("Failed to fetch standings for tournament $id: ${e.message}")
                                emptyList()
                            }
                        }
                    }
                    delay(200)  // Rate limiting
                    results.awaitAll().flatten()
                }
                .map { it.toStanding() }
        }
}
```

**Key Characteristics**:
- NOT an interface (direct implementation)
- Advanced async/await with chunking for rate limiting
- Error handling per item (try-catch inside async)
- Filtering logic (top 8, minimum players)
- Extension functions for DTO conversion (toTournamentId(), toStanding())

---

## 4. NETWORK SERVICE PATTERNS

### 4.1 Service Interface Pattern

**Interface** (`TournamentStatsService.kt`):
```kotlin
interface TournamentStatsService {
    suspend fun fetchTournaments(game: String): List<TournamentResponse>
    suspend fun fetchStandings(tournamentId: String): StandingsResponse
    
    companion object {
        const val BASE_URL = "https://play.limitlesstcg.com/api"
    }
}
```

**Key Characteristics**:
- Pure interface for HTTP operations
- Returns response DTOs directly (not domain models)
- Suspend functions for async/await
- BASE_URL in companion object

### 4.2 Service Implementation Pattern

**Implementation** (`DefaultTournamentStatsService.kt`):
```kotlin
class DefaultTournamentStatsService(
    private val client: HttpClient = httpClient,
    private val baseUrl: String = BASE_URL,
) : TournamentStatsService {
    override suspend fun fetchTournaments(game: String): List<TournamentResponse> =
        client.get("$baseUrl/tournaments?game=$game").body()
    
    override suspend fun fetchStandings(tournamentId: String): StandingsResponse =
        client.get("$baseUrl/tournaments/$tournamentId/standings").body()
}
```

**Key Characteristics**:
- Default HttpClient parameter (injectable for testing)
- Simple delegating methods
- URL building inline
- Direct deserialization with .body()

---

## 5. DATA TRANSFER OBJECTS (DTOs) AND MAPPING

### 5.1 DTO Response Pattern

**API Response DTOs** (`StandingsResponse.kt`):
```kotlin
@Serializable
data class StandingsResponse(val standings: List<StandingResponse>)

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
data class DeckResponse(val pokemon: List<String>? = null)

@Serializable
data class RecordResponse(
    val wins: Int,
    val losses: Int,
    val ties: Int,
)
```

### 5.2 Domain Model Pattern

**Domain Model** (same file as DTO):
```kotlin
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
```

### 5.3 DTO-to-Domain Mapping Pattern

**Extension Function Mapping** (in same file as DTO):
```kotlin
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
```

**Key Characteristics**:
- Mapping functions are extension functions on response objects
- Located in same file as DTO/response
- Named pattern: `SourceType.toDestinationType()`
- Flatten nested objects (PlayerResponse → playerName)
- Handle nullability (deck?.pokemon ?: emptyList())

---

## 6. DEPENDENCY INJECTION PATTERN

### 6.1 Manual DI via ViewModel Factories

**Pattern** (`TierDecksViewModel.kt`):
```kotlin
class TierDecksViewModel(
    decksRepo: DecksRepo,
) : ViewModel() {
    companion object {
        fun factory(decksRepo: DecksRepo): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(TierDecksViewModel::class.java)) {
                        return TierDecksViewModel(decksRepo) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}
```

**Key Characteristics**:
- Companion object factory function
- Takes dependencies as parameters
- Returns ViewModelProvider.Factory
- Used with `viewModel(factory = ViewModelClass.factory(...))`

### 6.2 Composition Root Pattern (in PocketDexApp.kt)

**Composition Root** (current manual DI):
```kotlin
composable(route = AllCards.route) {
    val allCardsViewModel: AllCardsViewModel =
        viewModel(
            factory = AllCardsViewModel.factory(
                cardsRepo = DefaultCardsRepo(
                    remoteCardsDataSource = RemoteCardsDataSource(
                        cardsService = DefaultCardsService(),
                    ),
                ),
            ),
        )
    AllCardsScreen(viewModel = allCardsViewModel, ...)
}
```

**Key Characteristics**:
- Dependencies created at point of use
- No centralized container
- Potential duplication (AllCards and CardDetail both create DefaultCardsRepo)
- Easy for testing (can pass fakes)

---

## 7. TESTING PATTERNS

### 7.1 Fake Repository Pattern

**Fake Implementation** (`FakeCardsRepo.kt`):
```kotlin
class FakeCardsRepo(
    private val cards: List<CardData> = fakeCardsData,
    private val cardDetail: CardDetail = fakeCardDetail,
    private val relatedCards: List<CardData> = fakeRelatedCards,
) : CardsRepo {
    override suspend fun allCards(): List<CardData> = cards
    override suspend fun cardDetail(id: String): CardDetail = cardDetail
    override fun relatedCards(id: String): List<CardData> = relatedCards
}
```

**Key Characteristics**:
- Implements real repository interface
- Takes dependencies as constructor parameters
- Uses companion object for fake data
- All functions are non-blocking

### 7.2 Fake Data Pattern

**Companion Object Fake Data** (`FakeCardsRepo.kt`):
```kotlin
companion object {
    val fakeCardsData = listOf(
        CardData(
            name = "Bulbasaur",
            imageUrl = "...",
            rarityUrl = "...",
            typeUrl = "...",
        ),
        // ... more cards
    )
    
    val fakeCardDetail = CardDetail(
        category = "Pokemon",
        name = "Venusaur ex",
        // ... more fields
    )
    
    val fakeRelatedCards = fakeCardsData.subList(0, 5)
}
```

**Key Characteristics**:
- Centralized in companion object
- Reusable across tests and previews
- Follows realistic data structure
- Can be parameterized in constructor

---

## 8. ASYNC/AWAIT PATTERNS

### 8.1 Parallel Data Fetching

**Pattern** (`RemoteCardsDataSource.kt`):
```kotlin
override suspend fun allCards(): List<CardData> =
    coroutineScope {
        val deferredCards = setIds.map { setId ->
            async { cardsService.briefCards(setId).cards }
        }
        deferredCards.awaitAll().flatten().toCardDataList()
    }
```

**Key Characteristics**:
- coroutineScope for structured concurrency
- async {} creates deferred values
- map {} to create list of deferreds
- awaitAll() waits for all results
- flatten() combines nested lists
- All within single suspend function

### 8.2 Chunked Async with Rate Limiting

**Pattern** (`RemoteTournamentDataSource.kt`):
```kotlin
override suspend fun getTop8Standings(tournamentIds: List<String>): List<Standing> =
    coroutineScope {
        tournamentIds
            .chunked(5)  // Process 5 at a time
            .flatMap { chunk ->
                val results = chunk.map { id ->
                    async {
                        try {
                            tournamentStatsService.fetchStandings(id).standings
                                .filter { it.placing <= 8 }
                        } catch (e: Exception) {
                            println("Failed to fetch standings for tournament $id: ${e.message}")
                            emptyList()
                        }
                    }
                }
                delay(200)  // Rate limiting between chunks
                results.awaitAll().flatten()
            }
            .map { it.toStanding() }
    }
```

**Key Characteristics**:
- chunked() for batch processing
- flatMap() combines batches
- Try-catch inside async for per-item error handling
- delay() for rate limiting between chunks
- Filter on API response before transformation

---

## 9. NAMING CONVENTIONS

### 9.1 Repository Naming
- Interface: `[Entity]Repo` (e.g., `CardsRepo`, `DecksRepo`)
- Default Implementation: `Default[Entity]Repo`
- Fake/Test Implementation: `Fake[Entity]Repo`

### 9.2 Data Source Naming
- Interface: `[Entity]DataSource` (e.g., `CardsDataSource`)
- Remote Implementation: `Remote[Entity]DataSource`
- Local Implementation: `Local[Entity]DataSource`
- (RemoteTournamentDataSource breaks this - no interface)

### 9.3 Service Naming
- Interface: `[Entity]Service` (e.g., `CardsService`, `TournamentStatsService`)
- Implementation: `Default[Entity]Service`

### 9.4 DTO/Response Naming
- API Response: `[Entity]Response` (e.g., `StandingResponse`, `TournamentResponse`)
- Composite Response: `[Entity]sResponse` (e.g., `StandingsResponse`)
- Domain Model: `[Entity]` (e.g., `Standing`, `Tournament`)

### 9.5 Mapper Function Naming
- Pattern: `Source.toDestination()` (e.g., `StandingResponse.toStanding()`)
- Pattern: `Collection.to[Type]List()` (e.g., `List<BriefCard>.toCardDataList()`)

### 9.6 ViewModel/Factory Naming
- ViewModel: `[Entity][Feature]ViewModel` (e.g., `AllCardsViewModel`, `TierDecksViewModel`)
- Factory: `companion object factory(...)` method

---

## 10. KEY PATTERNS FOR STATISTICS AGGREGATION

### 10.1 Applicable Patterns for DeckStatisticsRepo

From the analysis, a `DeckStatisticsRepo` should follow:

**Interface Pattern**:
```kotlin
interface DeckStatisticsRepo {
    suspend fun aggregateDeckStatistics(tournamentIds: List<String>): List<DeckStatistics>
    suspend fun getDeckWinRate(deckPokemon: List<String>): DeckWinRate?
    suspend fun getDeckMetaShare(deckPokemon: List<String>): Double
}
```

**Data Source Pattern**:
```kotlin
interface DeckStatisticsDataSource {
    suspend fun aggregateFromStandings(standings: List<Standing>): List<DeckStatistics>
}

class RemoteDeckStatisticsDataSource(
    private val tournamentDataSource: RemoteTournamentDataSource,
) : DeckStatisticsDataSource {
    // Uses RemoteTournamentDataSource for standings data
    // Aggregates and transforms to statistics
}
```

**No-Interface Pattern**:
- Could follow RemoteTournamentDataSource pattern (no interface) if:
  - Very specialized logic
  - Not expected to have multiple implementations
  - Tight coupling to source is acceptable

### 10.2 Domain Model Pattern for Statistics

**Following existing patterns**:
```kotlin
// Domain Model
data class DeckStatistics(
    val deckPokemon: List<String>,
    val appearances: Int,
    val wins: Int,
    val losses: Int,
    val winRate: Double,
    val metaShare: Double,
)

// DTO (if needed from API)
@Serializable
data class DeckStatsResponse(...)

// Mapper
fun calculateFromStandings(standings: List<Standing>): List<DeckStatistics> {
    // Aggregation logic
}
```

### 10.3 Async/Rate Limiting for Aggregation

**Following tournament patterns**:
```kotlin
// Get tournaments
val tournamentIds = getQualifyingTournamentIds()

// Fetch standings with rate limiting (chunked)
val standings = getTop8Standings(tournamentIds)

// Aggregate into statistics
val stats = aggregateFromStandings(standings)
```

---

## 11. ANTI-PATTERNS TO AVOID

1. **Mixing Response DTOs and Domain Models in UI**
   - Current code shows this in some places (FakeCardsRepo uses CardData directly)
   - Better: Always map to domain models at data source layer

2. **Async Operations Without Error Handling**
   - Current: Some async calls don't handle exceptions
   - Better: Use try-catch within async blocks

3. **Unstructured Concurrency**
   - Good: RemoteCardsDataSource uses coroutineScope
   - Bad: Creating coroutines without proper scope

4. **Missing Interfaces for Testability**
   - Good: CardsDataSource has interface
   - Bad: RemoteTournamentDataSource has no interface (less testable)

5. **DI Duplication at Composition Root**
   - Current: DefaultCardsRepo created twice in PocketDexApp
   - Better: Cache or create once at app level

---

## 12. SUMMARY OF KEY TAKEAWAYS

### Separation of Concerns
```
Repository: Orchestration, single source of truth
DataSource: Raw data access, transformation
Service: HTTP communication only
```

### Async Best Practices
- Use coroutineScope for structured concurrency
- Map to async {} for parallel operations
- awaitAll() to collect results
- Try-catch for per-item error handling
- delay() and chunking for rate limiting

### Testing Strategy
- Interface-based repositories for easy mocking
- Fake implementations for UI previews
- Companion object for shared fake data
- Constructor parameters for customizable test data

### Mapping Pattern
- Extension functions named `Source.toDestination()`
- Located in response DTO file
- Flatten nested structures
- Handle nullability with elvis operator

### DI Pattern
- Manual DI via ViewModel factories
- Composition at point of use
- Easy to test and replace

---

## 13. FILE STRUCTURE REFERENCE

```
app/src/main/java/tcg/pocket/dex/
├── repo/
│   ├── allcards/
│   │   ├── CardsRepo.kt (interface)
│   │   ├── DefaultCardsRepo.kt (implementation)
│   │   └── FakeCardsRepo.kt (testing)
│   └── decks/
│       ├── DecksRepo.kt (interface)
│       └── FakeDecksRepo.kt (fake)
├── datasource/
│   ├── CardsDataSource.kt (interface)
│   ├── RemoteCardsDataSource.kt (implementation)
│   └── RemoteTournamentDataSource.kt (no interface)
└── remote/
    ├── service/
    │   ├── CardsService.kt (interface)
    │   ├── DefaultCardsService.kt (implementation)
    │   ├── TournamentStatsService.kt (interface)
    │   └── DefaultTournamentStatsService.kt (implementation)
    └── response/
        ├── *Response.kt (DTOs)
        └── [domain model + mapping]
```

