# Null Handling Quick Reference Guide

## Quick Patterns to Use

### 1. Safe API Response Mapping
```kotlin
// For nullable lists
val items: List<Item> = response.items?.map { it.toDomain() } ?: emptyList()

// For nullable single values
val value: String = response.value ?: "default"

// For nested optionals
val nested: String = response.data?.field?.subfield ?: "default"

// For collection operations
val first: Item? = response.items?.firstOrNull()
```

### 2. Domain Model Validation
```kotlin
data class MyData(
    val required: String,
    val optional: String? = null,
    val items: List<Item> = emptyList(),  // Never null!
) {
    init {
        require(required.isNotEmpty()) { "Required field cannot be empty" }
    }
}
```

### 3. Collection Handling
```kotlin
// Empty list check before rendering
if (items.isEmpty()) {
    return  // Early return
}

// Safe operations
items.map { it.transform() }  // Maps to list, never null
items.filter { it.isValid() }  // Filters safely
items.firstOrNull()            // Safe first access
```

### 4. String Handling
```kotlin
// Empty string as null replacement
val url: String = response.image ?: "" + "/path"

// String validation
if (text.isNotEmpty()) {
    // Process text
}

// Safe substring/operations
val result = text.takeIf { it.isNotEmpty() }?.uppercase() ?: "DEFAULT"
```

### 5. Enum Parsing with Fallback
```kotlin
enum class Status { ACTIVE, INACTIVE, UNKNOWN }

fun fromString(value: String): Status = when(value) {
    "active" -> Status.ACTIVE
    "inactive" -> Status.INACTIVE
    else -> Status.UNKNOWN  // Always have default!
}
```

### 6. Error Handling
```kotlin
// Graceful degradation
try {
    return service.fetch()
} catch (e: Exception) {
    logger.error("Failed: ${e.message}")
    return emptyList()  // Return safe default
}
```

### 7. Computed Properties
```kotlin
data class Item(
    val name: String,
    val description: String,
) {
    val hasDescription: Boolean
        get() = description.isNotEmpty()
}

// Usage
if (item.hasDescription) {
    showDescription(item.description)
}
```

---

## Do's and Don'ts

| Do | Don't |
|----|-------|
| Use `emptyList()` for optional collections | Never pass `null` for collections |
| Mark nullable types with `?` | Assume fields are always present |
| Use `?.` for safe access | Chain multiple null checks |
| Provide sensible defaults | Let null propagate to UI |
| Validate in `init` blocks | Allow invalid states to exist |
| Test edge cases | Skip null/empty test cases |
| Use computed properties | Scatter state checks in UI |
| Catch and fallback on errors | Let exceptions crash the app |
| Document nullable fields | Leave nullability ambiguous |

---

## Common Scenarios

### API Response with Many Optional Fields
```kotlin
@Serializable
data class ApiResponse(
    val id: String,                    // Required
    val name: String,                  // Required
    val description: String? = null,   // Optional
    val tags: List<String>? = null,    // Optional
    val metadata: Metadata? = null,    // Optional
)

fun ApiResponse.toDomain(): DomainObject = DomainObject(
    id = id,
    name = name,
    description = description ?: "",           // String default
    tags = tags ?: emptyList(),               // List default
    metadata = metadata?.toDomain(),          // Preserve nullable
)
```

### Safe List Operations
```kotlin
val items: List<Item> = emptyList()

// All safe - no null checks needed
items.map { it.name }
items.filter { it.isActive }
items.firstOrNull() ?: Item.NONE
items.ifEmpty { listOf(Item.DEFAULT) }
```

### Nullable Domain Fields (When Appropriate)
```kotlin
data class Tournament(
    val id: String,
    val name: String,
    val country: String?,              // Can be null - semantically valid
    val region: String?,               // Can be null - semantically valid
    val participants: List<Player>,    // Never null - always a list
)
```

### UI-Level Null Handling
```kotlin
@Composable
fun MyScreen(items: List<Item>) {
    Column {
        // Early return for empty state
        if (items.isEmpty()) {
            Text("No items")
            return@Column
        }
        
        // Safe rendering
        items.forEach { item ->
            ItemView(item)
        }
    }
}
```

---

## Validation Checklist

Before shipping code, ensure:

- [ ] All required fields are non-nullable
- [ ] Optional fields explicitly marked with `?`
- [ ] Collections never null (use `emptyList()`)
- [ ] Validation happens in `init` blocks
- [ ] API responses handle all optional fields
- [ ] Domain mapping uses safe operators (`?.`, `?:`)
- [ ] UI checks `isEmpty()` not null
- [ ] Error handling returns safe defaults
- [ ] Edge cases tested (null, empty, extreme values)
- [ ] No scattered null checks in UI code

---

## File Locations for Reference

- **Domain Models**: `Card.kt`, `CardDeck.kt`
- **API Response Mapping**: `CardDetailResponse.kt`, `StandingsResponse.kt`
- **Data Source Error Handling**: `RemoteTournamentDataSource.kt`
- **UI-Level Handling**: `PokemonMovesSection.kt`
- **Enum Parsing**: `Rarity.kt`
- **Tests**: `RemoteTournamentDataSourceTest.kt`, `StandingsResponseTest.kt`
