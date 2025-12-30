# Theme 2: Card-Gallery (Visual Showcase)

## Description
Emphasizes visual Pokémon card presentation with large, tappable images. 
Perfect for players who prefer visual browsing and want to see card artwork prominently. 
**Displays Pokémon cards only** (based on available tournament data and CardsRepo API).

## ASCII Wireframe

```
┌─────────────────────────────────────┐
│ ← Pikachu + Mewtwo          ⋮       │ ← TopAppBar
├─────────────────────────────────────┤
│                                      │
│  ┌────────────────────────────────┐ │
│  │ ┏━━━━━━┓ ┏━━━━━━┓ ┏━━━━━━┓   │ │
│  │ ┃      ┃ ┃      ┃ ┃      ┃   │ │
│  │ ┃ HERO ┃ ┃ HERO ┃ ┃ HERO ┃   │ │ ← HorizontalPager
│  │ ┃ IMG  ┃ ┃ IMG  ┃ ┃ IMG  ┃   │ │   Carousel
│  │ ┃      ┃ ┃      ┃ ┃      ┃   │ │
│  │ ┗━━━━━━┛ ┗━━━━━━┛ ┗━━━━━━┛   │ │
│  │        ⚪⚫⚪                  │ │ ← Indicators
│  └────────────────────────────────┘ │
│                                      │
│  Pikachu + Mewtwo EX                │ ← Title
│  #3 Tier │ [⚡][⚫]                  │ ← Rank & Types
│                                      │
│  ┌──────────┬──────────┐            │
│  │ 65.5%    │ 25.0%    │            │ ← Quick Stats
│  │ Win Rate │ Usage    │            │   Chips
│  └──────────┴──────────┘            │
│                                      │
│  ━━━ KEY POKÉMON ━━━━━━━━━━━━━━━  │
│                                      │
│  ╔═══════╗  ╔═══════╗  ╔═══════╗  │
│  ║       ║  ║       ║  ║       ║  │
│  ║ CARD  ║  ║ CARD  ║  ║ CARD  ║  │ ← Large Card
│  ║ IMAGE ║  ║ IMAGE ║  ║ IMAGE ║  │   Grid 2 cols
│  ║       ║  ║       ║  ║       ║  │
│  ╚═══════╝  ╚═══════╝  ╚═══════╝  │
│  Pikachu EX  Mewtwo EX  Zapdos     │
│  ⚡ 60 HP    ⚫ 120 HP   ⚡ 90 HP   │
│                                      │
│  ━━━ ALL POKÉMON ━━━━━━━━━━━━━━━  │
│  ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐  │
│  │     │ │     │ │     │ │     │  │ ← LazyVertical
│  │ IMG │ │ IMG │ │ IMG │ │ IMG │  │   Grid 4 cols
│  │     │ │     │ │     │ │     │  │   Compact
│  └─────┘ └─────┘ └─────┘ └─────┘  │
│   Pika    Mew     Zap    (scroll)  │
│                                      │
└─────────────────────────────────────┘
```

## Key Design Decisions

1. **Visual First**: Large Pokémon images as primary content
2. **Carousel Hero**: Swipeable representative Pokémon cards at top
3. **Grid Layout**: 2-column grid for key Pokémon, 4-column for all Pokémon
4. **Pokémon Only**: Shows only Pokémon cards (tournament data limitation)
5. **Immersive**: Minimal text, maximum visual card presence
6. **Touch-Friendly**: Large tap targets on card images

## Component Breakdown

```kotlin
@Composable
fun CardGalleryThemeDeckDetail() {
    Scaffold(topBar = { /* TopAppBar */ }) {
        LazyColumn {
            // Hero Image Carousel
            item {
                HorizontalPager(
                    pageCount = pokemonCards.size
                ) { page ->
                    CardImageHero(pokemonCards[page].imageUrl)
                }
                HorizontalPagerIndicator()
            }

            // Title and Quick Stats
            item {
                Column {
                    Text(deckName, style = MaterialTheme.typography.headlineMedium)
                    Row {
                        RankBadge(rank)
                        TypeChips(types)
                    }
                    Row {
                        StatChip("Win Rate", winRate)
                        StatChip("Usage", share)
                    }
                }
            }

            // Key Pokémon Section
            item { SectionDivider("KEY POKÉMON") }
            item {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    userScrollEnabled = false
                ) {
                    items(pokemonCards) { LargePokemonCardItem(it) }
                }
            }

            // All Pokémon Grid
            item { SectionDivider("ALL POKÉMON") }
            item {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    userScrollEnabled = false
                ) {
                    items(pokemonCards) { CompactPokemonCardItem(it) }
                }
            }
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
  - Pokémon card images (high-quality)
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
Visual learners and collectors who want to browse Pokémon card artwork and appreciate design.

## Pros
- **95% implementable** with current APIs
- Beautiful, immersive visual experience
- Easy browsing with large images
- Great for card collectors
- Intuitive carousel navigation
- Clear display of available data

## Cons
- Pokémon cards only (no trainers/items)
- No card quantities shown
- May load slower with many high-res images
- Higher data usage

## Implementation Notes

### Fetching Pokémon Cards
```kotlin
// Parse Pokémon names from deckId
val pokemonNames = deckId.split("|") // ["Pikachu", "Mewtwo"]

// Fetch each Pokémon card details
val pokemonCards = pokemonNames.mapNotNull { name ->
    val searchResults = cardsRepo.search(name)
    val match = findBestMatch(searchResults, name)
    match?.let { cardsRepo.cardDetail(it.id) }
}
```

### Name Matching Strategy
1. Exact match: "Pikachu"
2. "ex" variant: "Pikachu ex"
3. Contains match: "Flying Pikachu"
4. Prioritize competitive variants

### Performance Optimization
- Parallel card fetching with coroutines
- Image lazy loading with Coil
- CardDetail caching in ViewModel
- Progressive image loading (low → high res)
