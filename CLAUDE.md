# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

TcgPocketDex is an Android app for browsing Pokémon TCG Pocket cards, decks, and expansion packs. Built with Jetpack Compose, it provides a native Android experience with plans for future KMP (Kotlin Multiplatform) migration.

## Build Commands

```bash
# Build the project
./gradlew build

# Run unit tests
./gradlew test

# Run unit tests for a specific module
./gradlew :app:test
./gradlew :lib-stringmatcher:test

# Run lint checks (ktlint)
./gradlew ktlintCheck

# Auto-format code with ktlint
./gradlew ktlintFormat

# Build debug APK
./gradlew assembleDebug

# Run instrumented tests (requires emulator/device)
./gradlew connectedAndroidTest
```

## Architecture

### Multi-Module Structure

- **app**: Main Android application module
- **lib-stringmatcher**: Pure Kotlin library for string matching algorithms (Rabin-Karp, English string matching)

### Layered Architecture (app module)

The app follows a clean architecture pattern with clear separation of concerns:

1. **UI Layer** (`*.screen`, `*.component`):
   - Jetpack Compose screens and reusable components
   - ViewModels handle UI state and business logic
   - Navigation handled via Compose Navigation with type-safe destinations in `navigation/PocketDexDestination.kt`

2. **Repository Layer** (`repo.*`):
   - Abstract data access behind repository interfaces
   - `CardsRepo`: Card data operations
   - `DecksRepo`: Deck data operations
   - Repositories coordinate between data sources

3. **Data Source Layer** (`datasource.*`):
   - `RemoteCardsDataSource`: Fetches data from external API
   - Currently no local caching (future enhancement)

4. **Network Layer** (`remote.*`):
   - `CardsService`: Ktor HTTP client interface
   - `DefaultCardsService`: Implementation using Ktor
   - Response DTOs in `remote/response/`
   - Base URL: Fetches from external TCG Pocket API

### Domain Models

**Card Type Hierarchy** (`Card.kt`):
The sealed class `Card` represents different card types with specialized attributes:
- `Card.Pokemon`: Has `BattleAttributes` (type, HP, moves, weakness, retreat cost), `FlavorText` (dex ID, species, description), and `Evolution` (stage, evolve from/to)
- `Card.Support`: Trainer support cards
- `Card.Item`: Trainer item cards

Each card has a `Code` (number + expansion pack) for identification.

**Response-to-Domain Mapping**:
- API responses (`*Response.kt`) are mapped to domain models or UI data classes
- Example: `CardDetailResponse.toCardDetail()` in `CardDetailResponse.kt:132`
- Domain models enforce business rules (e.g., `Evolution` validation in `Card.kt:63-65`)

### Navigation Structure

Three main bottom bar destinations (defined in `PocketDexDestination.kt`):
- **AllCards**: Browse all available cards
- **TierDecks**: View competitive tier decks
- **ExpansionPacks**: Browse cards by expansion set

Detail screens:
- **CardDetail**: Detailed card view with stats, moves, related decks
- **TierDeckDetail**: Full deck composition and strategy
- **Search**: Context-aware search (cards/decks/packs)
- **Setting**: App settings

### Dependency Injection

Currently uses **manual dependency injection** via factory functions in ViewModels. Dependencies are constructed at the call site in `PocketDexApp.kt`. DI framework (like Hilt) is a future enhancement.

### Testing Strategy

- **Unit tests** in `src/test/`: Domain logic, ViewModels, utilities (e.g., `CardTest.kt`, `EvolutionTest.kt`)
- **Instrumented tests** in `src/androidTest/`: UI tests with Compose Testing, Navigation tests
- `lib-stringmatcher` uses Kotest for property-based testing

## Development Notes

### Code Style

- Kotlin coding conventions enforced by ktlint
- Run `./gradlew ktlintFormat` before committing

### Known TODOs from README

The project is actively evolving. From the README and code comments:
- Domain layer cleanup: Currently mixing response objects and domain models in UI layer
- Related cards functionality: Currently disabled, needs implementation when API supports it
- Card type handling: Support/Item cards need different UI treatment (no HP, moves, etc.)
- Rarity matching: Rarity enum mapping needs refinement
- Dependency injection: Consider Hilt for cleaner DI
- Local caching: Add Room database for offline support

### CI/CD

GitHub Actions workflow (`.github/workflows/Android_Dev_Ci.yml`) runs on PRs to `dev`:
1. ktlint check
2. Unit tests
3. Build
4. Gemini AI code review (provides feedback on Android best practices and future KMP considerations)

### Compose Development

The developer is learning Jetpack Compose. When reviewing or suggesting changes:
- Prefer idiomatic Compose patterns
- Explain state management and recomposition concepts
- Strong skipping mode is enabled (`enableStrongSkippingMode = true` in `app/build.gradle.kts:58`)
- Explicit backing fields language feature is enabled for advanced state management

### Future KMP Migration

While currently Android-native, the project plans eventual KMP migration:
- Avoid tight coupling to Android APIs where reasonable
- Network layer (Ktor) is already KMP-compatible
- UI layer will remain Android-specific (Compose Multiplatform would be separate effort)
