# Theme 1: Stats-Focused (Competitive Analytics)

## Description
Prioritizes competitive statistics and meta-game information. Ideal for players who want to quickly assess deck viability and performance metrics before diving into Pokémon composition. **Displays Pokémon cards only** (based on available tournament data and CardsRepo API).

## ASCII Wireframe

```
┌─────────────────────────────────────┐
│ ← Deck Details              ⋮       │ ← TopAppBar
├─────────────────────────────────────┤
│                                      │
│  ┌────────────────────────────────┐ │
│  │  TIER RANK #3                  │ │
│  │  ┌─────┐ ┌─────┐ ┌─────┐       │ │
│  │  │     │ │     │ │     │       │ │ ← Representative
│  │  │ IMG │ │ IMG │ │ IMG │       │ │   Pokemon Cards
│  │  │     │ │     │ │     │       │ │
│  │  └─────┘ └─────┘ └─────┘       │ │
│  │                                 │ │
│  │  Pikachu + Mewtwo EX           │ │ ← Deck Name
│  │  [⚡][⚫]                       │ │ ← Type Chips
│  └────────────────────────────────┘ │
│                                      │
│  ╔════════════════════════════════╗ │
│  ║  PERFORMANCE METRICS           ║ │
│  ╟────────────────────────────────╢ │
│  ║  Win Rate        Usage Share   ║ │
│  ║  ┌─────────┐    ┌─────────┐   ║ │
│  ║  │  65.5%  │    │  25.0%  │   ║ │ ← Stat Cards
│  ║  └─────────┘    └─────────┘   ║ │
│  ╚════════════════════════════════╝ │
│                                      │
│  ── KEY POKÉMON ──────────────────  │ ← Section Header
│  ┌────┐ ┌────┐ ┌────┐              │
│  │    │ │    │ │    │              │
│  │IMG │ │IMG │ │IMG │  >>>         │ ← Horizontal
│  │    │ │    │ │    │              │   Scroll
│  └────┘ └────┘ └────┘              │
│  Pikachu Mewtwo Zapdos              │
│  ⚡ 60HP ⚫120HP ⚡ 90HP             │
│                                      │
│  ── DECK POKÉMON ─────────────────  │ ← Section Header
│  ╭─────────────────────────────────╮│
│  │ Pikachu EX           [⚡] ★★★  ││
│  │ Mewtwo EX            [⚫] ★★★  ││ ← LazyColumn
│  │ Zapdos               [⚡] ★★   ││   List Items
│  ╰─────────────────────────────────╯│
│                                      │
└─────────────────────────────────────┘
```

## Key Design Decisions

1. **Stats Prominence**: Performance metrics in prominent card at top
2. **Visual Hierarchy**: Tier rank badge → Stats → Pokémon cards
3. **Scannable Layout**: Large numbers, clear labels, color-coded sections
4. **Pokémon Only**: Shows only Pokémon cards from tournament data
5. **Touch Targets**: 48dp minimum for all interactive elements

## Component Breakdown

```kotlin
@Composable
fun StatsThemeDeckDetail() {
    Scaffold(topBar = { /* TopAppBar with back */ }) {
        LazyColumn {
            // Hero Section
            item { DeckHeroCard(rank, images, name, types) }

            // Performance Metrics Card
            item {
                ElevatedCard {
                    Row {
                        StatColumn("Win Rate", winRate)
                        StatColumn("Usage", share)
                    }
                }
            }

            // Key Pokémon Horizontal Scroll
            item {
                SectionHeader("KEY POKÉMON")
                LazyRow {
                    items(pokemonCards) { KeyPokemonCardItem(it) }
                }
            }

            // Full Pokémon List
            item { SectionHeader("DECK POKÉMON") }
            items(pokemonCards) { DeckPokemonListItem(it) }
        }
    }
}
```

## Data Requirements

### Available from APIs (100%)
- **TournamentStatsRepo**:
  - Deck name (sorted Pokémon names)
  - Win rate percentage
  - Usage share percentage
  - Tournament appearances (for ranking)

- **CardsRepo** (via search + detail):
  - Pokémon card images
  - Pokémon HP
  - Pokémon type
  - Rarity
  - Evolution stage
  - Moves and abilities

### Not Available (Future Enhancement)
- Trainer cards (Support/Item)
- Card quantities (2×, 1×)
- Deck strategy descriptions
- Deck cost/points

## Target User
Competitive players who want quick meta analysis and deck viability assessment.

## Pros
- **90% implementable** with current APIs
- Fast information scanning
- Clear performance metrics
- Efficient for competitive decision-making
- Minimal scroll depth for key info
- Clean data presentation

## Cons
- Pokémon cards only (no trainers/items)
- No card quantities shown
- Less visual appeal for casual players
- Stats-heavy may intimidate beginners

## Implementation Notes

### Ranking Calculation
```kotlin
// Derive rank from tournament appearances
fun calculateRank(appearances: Int, allDecks: List<CalculatedDeck>): Int {
    return allDecks
        .sortedByDescending { it.appearances }
        .indexOfFirst { it.appearances == appearances } + 1
}
```

### Type Chips Extraction
```kotlin
// Extract unique types from Pokémon cards
val pokemonTypes = pokemonCards
    .map { it.type }
    .distinct()
    .map { PokemonTypeChipData(it) }
```

### Performance Optimization
- Single API call to TournamentStatsRepo
- Parallel Pokémon card fetching
- Minimal UI complexity for fast rendering
- Efficient list rendering with LazyColumn
