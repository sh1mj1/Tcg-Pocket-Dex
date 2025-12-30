# Theme 3: Pokémon-Overview (Simplified Information)

## Description
Simplified deck overview focusing on Pokémon composition and performance metrics. **Displays only Pokémon cards** (based on available tournament data and CardsRepo API). This theme has been significantly simplified from the original "Strategic-Guide" concept due to lack of strategy data in APIs.

## ASCII Wireframe

```
┌─────────────────────────────────────┐
│ ← Deck Overview             ⋮       │ ← TopAppBar
├─────────────────────────────────────┤
│                                      │
│  ╔════════════════════════════════╗ │
│  ║ TIER #3 │ PIKACHU + MEWTWO EX ║ │
│  ╟────────────────────────────────╢ │
│  ║ ┌──┐ ┌──┐ ┌──┐     [⚡][⚫]   ║ │ ← Compact Header
│  ║ │▓▓│ │▓▓│ │▓▓│               ║ │   Card
│  ║ └──┘ └──┘ └──┘               ║ │
│  ╚════════════════════════════════╝ │
│                                      │
│  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓  │
│  ┃ 📊 PERFORMANCE               ┃  │
│  ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫  │
│  ┃ Win Rate: 65.5%              ┃  │
│  ┃ Usage Share: 25.0%           ┃  │
│  ┃ Tournament Appearances: 42   ┃  │
│  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛  │
│                                      │
│  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓  │
│  ┃ 🎴 KEY POKÉMON               ┃  │
│  ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫  │
│  ┃ ┌────┬───────────────────┐   ┃  │
│  ┃ │    │ PIKACHU EX        │   ┃  │
│  ┃ │IMG │ ⚡ 60 HP | ★★★   │   ┃  │
│  ┃ └────┴───────────────────┘   ┃  │
│  ┃ ┌────┬───────────────────┐   ┃  │
│  ┃ │    │ MEWTWO EX         │   ┃  │
│  ┃ │IMG │ ⚫ 120 HP | ★★★   │   ┃  │
│  ┃ └────┴───────────────────┘   ┃  │
│  ┃ ┌────┬───────────────────┐   ┃  │
│  ┃ │    │ ZAPDOS            │   ┃  │
│  ┃ │IMG │ ⚡ 90 HP | ★★    │   ┃  │
│  ┃ └────┴───────────────────┘   ┃  │
│  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛  │
│                                      │
└─────────────────────────────────────┘
```

## Key Design Decisions

1. **Simplified Approach**: Focuses on available data only
2. **Performance First**: Tournament statistics prominently displayed
3. **Pokémon Overview**: Detailed list of Pokémon in deck
4. **Clean Layout**: Organized card presentation with HP and rarity
5. **Minimal Sections**: Removed unavailable content (strategy, roles, trainers)

## Component Breakdown

```kotlin
@Composable
fun PokemonOverviewThemeDeckDetail() {
    Scaffold(topBar = { /* TopAppBar */ }) {
        LazyColumn {
            // Compact Header Card
            item {
                CompactDeckHeaderCard(
                    rank, deckName, images, types
                )
            }

            // Performance Stats Card
            item {
                OutlinedCard {
                    Column {
                        Text("📊 PERFORMANCE")
                        PerformanceMetrics(winRate, share, appearances)
                    }
                }
            }

            // Key Pokémon List
            item {
                OutlinedCard {
                    Text("🎴 KEY POKÉMON")
                    pokemonCards.forEach { pokemon ->
                        PokemonListItem(
                            imageUrl = pokemon.imageUrl,
                            name = pokemon.name,
                            type = pokemon.type,
                            hp = pokemon.hp,
                            rarity = pokemon.rarity
                        )
                    }
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
  - Tournament appearances

- **CardsRepo** (via search + detail):
  - Pokémon card images
  - Pokémon HP
  - Pokémon type
  - Rarity
  - Evolution stage
  - Moves and abilities

### Not Available (Removed from Design)
- Trainer cards (Support/Item)
- Card quantities (2×, 1×)
- Deck strategy content (Early/Mid/Late game)
- Card role descriptions
- Deck cost/points
- Meta position indicators
- Difficulty ratings

## Target User
Players who want a clean, straightforward overview of deck Pokémon composition and performance.

## Pros
- **85% implementable** with current APIs
- Simple and clean layout
- Focus on available data
- No misleading placeholder content
- Easy to understand
- Efficient rendering

## Cons
- Pokémon cards only (no trainers/items)
- No strategic guidance (original theme concept lost)
- No card quantities shown
- Less educational value than originally envisioned
- May not satisfy players seeking strategy help

## Implementation Notes

### Simplified from Original Concept
This theme was originally designed as "Strategic-Guide" with:
- Detailed strategy content (Early/Mid/Late game)
- Card role descriptions
- Meta positioning
- Difficulty ratings

However, due to API limitations, it has been simplified to a "Pokémon-Overview" theme that focuses only on displaying available data cleanly.

### Future Enhancement Path
If strategy data becomes available:
1. Add "DECK STRATEGY" section with gameplay guidance
2. Include card role descriptions
3. Add meta position and difficulty indicators
4. Restore educational focus of original concept

### Performance Metrics Display
```kotlin
@Composable
fun PerformanceMetrics(
    winRate: String,
    usageShare: String,
    appearances: Int
) {
    Column {
        MetricRow("Win Rate", winRate)
        MetricRow("Usage Share", usageShare)
        MetricRow("Tournament Appearances", appearances.toString())
    }
}
```

### Pokémon List Item
```kotlin
@Composable
fun PokemonListItem(
    imageUrl: String,
    name: String,
    type: PokemonType,
    hp: String,
    rarity: Rarity
) {
    Row {
        AsyncImage(model = imageUrl, modifier = Modifier.size(48.dp))
        Column {
            Text(name, style = MaterialTheme.typography.titleMedium)
            Row {
                TypeChip(type)
                Text("$hp HP")
                RarityIndicator(rarity)
            }
        }
    }
}
```

## Recommendation

**Not recommended for MVP** due to:
- Limited value compared to Theme 1 & 2
- Original educational concept lost
- Becomes redundant with simpler themes

**Consider for future** when:
- Strategy content API available
- Community-contributed deck guides ready
- Can restore original "Strategic-Guide" vision
