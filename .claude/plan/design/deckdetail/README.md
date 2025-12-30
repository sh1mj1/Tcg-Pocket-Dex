# Deck Detail Screen - Wireframe Design Options

## Overview

This directory contains three wireframe design themes for the Deck Detail Screen in TcgPocketDex. **All themes have been revised to show only Pokémon cards** based on actual data available from TournamentStatsRepo and CardsRepo APIs.

### API Constraints

**Available Data**:
- Tournament statistics (win rate, usage share, appearances)
- Pokémon names from deck composition
- Individual Pokémon card details (images, HP, type, rarity, moves)

**Not Available**:
- Trainer cards (Support/Item)
- Card quantities (2×, 1×)
- Deck strategy content
- Deck cost/points

## Available Themes

### 1. [Stats-Focused (Competitive Analytics)](./theme1-stats-focused.md)
**Target User**: Competitive players
**Primary Focus**: Performance metrics and Pokémon composition
**Implementation**: **90% feasible** with current APIs
**Best For**: Quick meta analysis and deck viability assessment

### 2. [Card-Gallery (Visual Showcase)](./theme2-card-gallery.md) ⭐ **MVP Recommended**
**Target User**: Visual learners and collectors
**Primary Focus**: Pokémon card imagery and visual browsing
**Implementation**: **95% feasible** with current APIs
**Best For**: Appreciating card artwork and visual exploration

### 3. [Pokémon-Overview (Simplified)](./theme3-strategic-guide.md)
**Target User**: Players wanting straightforward information
**Primary Focus**: Clean Pokémon list with performance data
**Implementation**: **85% feasible** with current APIs
**Note**: Simplified from original "Strategic-Guide" due to lack of strategy data

## Comparison Matrix

| Feature | Stats-Focused | Card-Gallery | Pokémon-Overview |
|---------|---------------|--------------|------------------|
| **Implementation** | 90% | **95%** ⭐ | 85% |
| **Primary Focus** | Performance metrics | Visual Pokémon | Clean overview |
| **Info Density** | High | Low | Medium |
| **Visual Weight** | Stats cards | Large images | Balanced |
| **Target User** | Competitive player | Visual learner | General user |
| **Scroll Depth** | Medium | Long | Short |
| **Interaction** | Quick scan | Browse cards | Simple view |
| **Data Limitations** | Pokémon only | Pokémon only | Pokémon only |
| **MVP Suitability** | Good | **Best** ⭐ | Fair |

## Available Data from APIs

### From TournamentStatsRepo ✅
```kotlin
data class CalculatedDeck(
    val deckId: String,        // "Pikachu|Mewtwo" (pipe-separated Pokémon names)
    val deckName: String,      // "Pikachu + Mewtwo"
    val winRate: String,       // "65.5%"
    val usageShare: String,    // "25.0%"
    val appearances: Int       // Tournament count
)
```

### From CardsRepo (via search + detail) ✅
```kotlin
data class CardDetail(
    val name: String,          // "Pikachu EX"
    val imageUrl: String,      // High-quality image
    val hp: String,            // "60"
    val type: String,          // "Electric"
    val rarity: String,        // "★★★"
    val moves: List<Move>,     // Attack moves
    val retreatCost: Int,      // Energy cost
    val stage: Int             // Evolution stage
)
```

### Data Flow
```
TournamentStatsRepo → deckId.split("|") → ["Pikachu", "Mewtwo"]
    ↓
CardsRepo.search("Pikachu") → Find match → CardDetail
    ↓
UI displays Pokémon with images, HP, type, rarity
```

### ❌ Not Available from APIs
- Trainer cards (Support/Item)
- Card quantities (2×, 1×)
- Deck strategy descriptions
- Deck cost/points
- Complete 20-card deck lists

## Implementation Recommendation

### Phase 1: MVP (Quick Launch) ⭐
**Recommended**: **Theme 2 (Card-Gallery)**

**Rationale**:
- **95% implementable** with current APIs (highest feasibility)
- Leverages CardsRepo visual strengths
- Minimal content creation needed
- Best user experience with available data
- Clear expectation setting (Pokémon only)

**MVP Features**:
1. Hero carousel with Pokémon images ✅
2. Win rate and usage stats ✅
3. Pokémon card grid (2-column key, 4-column all) ✅
4. Type chips from card data ✅

### Phase 2: Add Stats Elements
- Integrate Stats-Focused performance metrics card
- Add horizontal Pokémon scroll
- Implement efficient list views

### Phase 3: Future Enhancements
**When trainer card data available**:
- Add "Trainers" section
- Show complete 20-card deck lists
- Display card quantities

**When strategy content ready**:
- Add deck strategy section
- Include card role descriptions
- Implement gameplay guides

## Hybrid Approach

Consider combining best elements from each theme:

```
┌─────────────────────────────────────┐
│ ← Deck Details              ⋮       │
├─────────────────────────────────────┤
│ [HERO CAROUSEL] ← Card-Gallery      │
│ Pikachu + Mewtwo EX                 │
│ #3 Tier │ [⚡][⚫]                   │
│                                      │
│ [PERFORMANCE METRICS] ← Stats       │
│ Win Rate: 65.5% │ Usage: 25%        │
│                                      │
│ [STRATEGY PREVIEW] ← Strategic      │
│ "Aggressive early pressure..."      │
│ [Read More]                          │
│                                      │
│ [KEY CARDS] ← Card-Gallery          │
│ Large card grid (2 columns)         │
│                                      │
│ [DECK LIST] ← Stats-Focused         │
│ Efficient list view                 │
└─────────────────────────────────────┘
```

## Technical Considerations

### Material 3 Components
- **Scaffold**: Screen structure
- **TopAppBar**: Navigation
- **LazyColumn**: Main scrollable container
- **Card/ElevatedCard/OutlinedCard**: Content grouping
- **LazyRow/LazyVerticalGrid**: Card galleries
- **HorizontalPager**: Image carousels (Card-Gallery)
- **TabRow**: Content filtering (Card-Gallery)

### Accessibility Requirements
- Minimum 48dp touch targets
- Content descriptions for screen readers
- Sufficient color contrast (WCAG AA)
- Clear heading structure
- Logical focus order

### Performance Optimization
- **Stats-Focused**: Fastest, minimal image loading
- **Card-Gallery**: Image lazy loading required
- **Strategic-Guide**: Text rendering optimization

## Decision Framework

Choose theme based on:
- **MVP Speed**: Card-Gallery (95% ready, best UX)
- **Data Efficiency**: Stats-Focused (90% ready, minimal data)
- **Simplicity**: Pokémon-Overview (85% ready, basic)

**Our Recommendation**: **Theme 2 (Card-Gallery)** for MVP due to highest implementation feasibility and best visual experience with available data.

## Next Steps

1. **Implement Theme 2 (Card-Gallery)** as MVP
2. **Test with real tournament data** and CardsRepo
3. **Validate Pokémon name matching** accuracy
4. **Optimize image loading** performance
5. **Plan trainer card integration** for Phase 2
6. **Consider strategy content** sources for Phase 3

## Related Files

- MVP Progress: `../../mvp-progress.md`
- Implementation Plan: Section 1.2 "덱 상세 화면 완성"
- Data Models: `app/src/main/java/tcg/pocket/dex/tierdecks/DeckInformation.kt`
