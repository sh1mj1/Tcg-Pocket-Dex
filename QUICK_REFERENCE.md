# Quick Reference: Repository and Data Source Patterns

## Architecture Overview

```
┌─────────────────────────────────────────────┐
│           UI Layer                           │
│  (Compose Screens + ViewModels)             │
└──────────────┬──────────────────────────────┘
               │
┌──────────────▼──────────────────────────────┐
│      Repository Layer (Interfaces)          │
│  CardsRepo, DecksRepo, DeckStatisticsRepo  │
└──────────────┬──────────────────────────────┘
               │
┌──────────────▼──────────────────────────────┐
│     Data Source Layer (Orchestration)       │
│ RemoteCardsDataSource,                      │
│ RemoteTournamentDataSource                  │
└──────────────┬──────────────────────────────┘
               │
┌──────────────▼──────────────────────────────┐
│      Service Layer (HTTP Client)            │
│  CardsService, TournamentStatsService      │
└──────────────┬──────────────────────────────┘
               │
┌──────────────▼──────────────────────────────┐
│      Remote APIs                            │
│  TCGDex API, Limitless TCG API             │
└─────────────────────────────────────────────┘
```

## Repository Pattern Checklist

When creating a new repository, ensure:

- [ ] Create interface: `interface [Entity]Repo`
- [ ] Create implementation: `class Default[Entity]Repo implements [Entity]Repo`
- [ ] Create fake: `class Fake[Entity]Repo for testing`
- [ ] Return domain models (not DTOs)
- [ ] Delegate to data source
- [ ] Suspend functions for async operations
- [ ] Add to ViewModels as factory parameter
- [ ] Inject via ViewModel factory method

## Data Source Pattern Checklist

When creating a new data source:

- [ ] Create interface: `interface [Entity]DataSource`
- [ ] Create implementation: `class Remote[Entity]DataSource`
- [ ] Inject service(s) in constructor
- [ ] Use `coroutineScope` for async operations
- [ ] Handle errors: try-catch in async blocks
- [ ] Rate limiting: use `chunked()` and `delay()`
- [ ] Transformation: `@Serializable` DTOs → domain models
- [ ] Create mapper: `fun DTO.toDomainModel(): DomainModel`

## DTO to Domain Mapping Template

```kotlin
// In Response file (e.g., StandingsResponse.kt)

@Serializable
data class MyResponse(
    val id: String,
    val nested: NestedResponse,
)

@Serializable
data class NestedResponse(
    val value: String,
)

// Domain model (same file)
data class MyDomain(
    val id: String,
    val nestedValue: String,
)

// Mapper function (same file)
fun MyResponse.toMyDomain(): MyDomain =
    MyDomain(
        id = id,
        nestedValue = nested.value,
    )
```

## Async/Await Pattern Templates

### Simple Parallel Fetching
```kotlin
override suspend fun getData(): List<Item> =
    coroutineScope {
        val results = ids.map { id ->
            async { service.fetch(id) }
        }
        results.awaitAll().map { it.toItem() }
    }
```

### Chunked Fetching with Rate Limiting
```kotlin
override suspend fun getMany(ids: List<String>): List<Item> =
    coroutineScope {
        ids
            .chunked(5)  // Process 5 at a time
            .flatMap { chunk ->
                val results = chunk.map { id ->
                    async {
                        try {
                            service.fetch(id).toItem()
                        } catch (e: Exception) {
                            null
                        }
                    }
                }
                delay(200)  // Rate limiting
                results.awaitAll().filterNotNull()
            }
    }
```

## Manual DI Pattern Template

### ViewModel Factory
```kotlin
class MyViewModel(
    private val myRepo: MyRepo,
) : ViewModel() {
    companion object {
        fun factory(myRepo: MyRepo): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(MyViewModel::class.java)) {
                        return MyViewModel(myRepo) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}
```

### Composition Root (in PocketDexApp.kt)
```kotlin
composable(route = MyScreen.route) {
    val viewModel: MyViewModel = viewModel(
        factory = MyViewModel.factory(
            myRepo = DefaultMyRepo(
                dataSource = RemoteMyDataSource(
                    service = DefaultMyService(),
                ),
            ),
        ),
    )
    MyScreen(viewModel = viewModel)
}
```

## Naming Convention Quick Reference

| Component | Naming Pattern | Example |
|-----------|---|---|
| Repository Interface | `[Entity]Repo` | `CardsRepo`, `DecksRepo` |
| Repository Implementation | `Default[Entity]Repo` | `DefaultCardsRepo` |
| Repository Fake | `Fake[Entity]Repo` | `FakeCardsRepo` |
| Data Source Interface | `[Entity]DataSource` | `CardsDataSource` |
| Remote Data Source | `Remote[Entity]DataSource` | `RemoteCardsDataSource` |
| Service Interface | `[Entity]Service` | `CardsService` |
| Service Implementation | `Default[Entity]Service` | `DefaultCardsService` |
| Response DTO | `[Entity]Response` | `StandingResponse` |
| Multiple Items Response | `[Entity]sResponse` | `StandingsResponse` |
| Domain Model | `[Entity]` | `Standing`, `Card` |
| Mapper Function | `Source.toDestination()` | `Standing.toStanding()` |
| Mapper for Lists | `List<Source>.to[Type]List()` | `List<BriefCard>.toCardDataList()` |
| ViewModel | `[Entity][Feature]ViewModel` | `AllCardsViewModel` |

## File Organization Template

```
repo/[entity]/
├── [Entity]Repo.kt          (interface)
├── Default[Entity]Repo.kt   (implementation)
└── Fake[Entity]Repo.kt      (for testing)

datasource/
├── [Entity]DataSource.kt         (interface)
└── Remote[Entity]DataSource.kt   (implementation)

remote/service/
├── [Entity]Service.kt           (interface)
└── Default[Entity]Service.kt    (implementation)

remote/response/
└── [Entity]Response.kt  (DTOs + domain models + mappers)
```

## For Deck Statistics Aggregation

### Step 1: Create Interfaces
```
repo/statistics/
├── DeckStatisticsRepo.kt (interface)
└── DefaultDeckStatisticsRepo.kt (implementation)

datasource/
├── DeckStatisticsDataSource.kt (interface)
└── RemoteDeckStatisticsDataSource.kt (implementation)
```

### Step 2: Create Domain Models
```
remote/response/
└── DeckStatisticsResponse.kt (DTOs + domain models + mappers)
```

### Step 3: Implement Data Flow
```
TournamentDataSource.getTop8Standings()
    ↓
DeckStatisticsDataSource.aggregateFromStandings()
    ↓
DeckStatisticsRepo.aggregateDeckStatistics()
    ↓
ViewModel → UI
```

### Step 4: Wire into DI
```kotlin
composable(route = DeckStats.route) {
    val viewModel: DeckStatsViewModel = viewModel(
        factory = DeckStatsViewModel.factory(
            deckStatsRepo = DefaultDeckStatisticsRepo(
                dataSource = RemoteDeckStatisticsDataSource(
                    tournamentDataSource = RemoteTournamentDataSource(
                        service = DefaultTournamentStatsService(),
                    ),
                ),
            ),
        ),
    )
    DeckStatsScreen(viewModel = viewModel)
}
```

## Common Mistakes to Avoid

| Mistake | Why Bad | Fix |
|---------|---------|-----|
| Return DTOs from repository | Type-unsafe, couples UI to API | Always convert to domain models |
| No error handling in async | Crashes on API failure | Use try-catch in async blocks |
| Async without coroutineScope | Unstructured concurrency | Always use coroutineScope/supervisorScope |
| No interface for data source | Hard to test, can't mock | Create interface first |
| Creating repos at composition root | Duplication, hard to change | Create factory methods |
| Blocking operations in UI layer | Jank, ANR crashes | Always use suspend/coroutines |
| Mixed suspend and sync | Inconsistent, confusing | Pick one, be consistent |

## Testing Strategy

### Unit Test Fake Repository
```kotlin
@Test
fun testFakeRepository() {
    val repo = FakeCardsRepo()
    assertEquals(fakeCardsData, repo.allCards())
}
```

### Instrumented Test with Real Repository
```kotlin
@Test
fun testRealRepository() = runTest {
    val service = DefaultCardsService()
    val dataSource = RemoteCardsDataSource(service)
    val repo = DefaultCardsRepo(dataSource)
    
    val cards = repo.allCards()
    assertTrue(cards.isNotEmpty())
}
```

### ViewModel with Fake Repository
```kotlin
@Test
fun testViewModel() {
    val viewModel = AllCardsViewModel(FakeCardsRepo())
    assertEquals(fakeCardsData, viewModel.cardsState.value)
}
```

## Key Dependencies

```
coroutineScope: Structured concurrency, parallel operations
async: Create deferred computation
awaitAll(): Collect async results
delay(): Rate limiting between operations
chunked(): Batch processing
flatMap(): Combine batched results
map(): Transform items
filter(): Filter items
```

## Performance Considerations

| Operation | Concern | Solution |
|-----------|---------|----------|
| Fetching many items | Network overload | Use chunked() with delay() |
| Serial async calls | Slow | Use parallel: async/awaitAll |
| Large result sets | Memory | Consider pagination |
| Repeated API calls | Bandwidth | Cache at repository level |
| Null handling | NullPointerException | Use elvis operator ?: |

