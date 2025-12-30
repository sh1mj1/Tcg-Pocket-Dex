# TierDecksScreen UI 수정 우선순위 분석

**생성일**: 2025-12-18
**상태**: 계획 완료 - 구현 대기
**컨텍스트**: PR #41 머지 후 TierDecksScreen에서 발견된 UI 이슈 분석

---

## 🎯 핵심 결론

**결정**: 문제 1, 5, 2를 즉시 수정 (기존 이슈 #38-40보다 우선)

**근거**: 사용자 대면 버그로 앱이 고장난 것처럼 보임. MVP 신뢰도를 해치므로 DeckDetailScreen 구현 전에 수정 필요.

**일정 영향**: ~2-3시간 작업, 로드맵에 최소 지연

---

## 📊 문제 분석 결과

### ✅ 문제 3: Win/Share Rate (조치 불필요)

**상태**: **정상 동작 중** ✅

**증거** (`CalculatedDeck.kt:15-20`):
```kotlin
data class CalculatedDeck(
    val deckId: String,
    val deckName: String,
    val winRate: String,      // 실제 API 데이터
    val usageShare: String,   // 실제 API 데이터
    val appearances: Int,
)
```

**발견 사항**: Limitless TCG API에서 `DeckStatsAggregator`를 통해 받아온 **실제 데이터**. 가짜 데이터 아님.

**조치**: 없음

---

### 🔴 문제 1: 포켓몬 이미지가 물음표로 표시 (심각한 버그)

**상태**: **즉시 수정 필요** 🚨

**원인** (`DefaultDecksRepo.kt:28-31`):
```kotlin
representativePokemonImageUrls =
    pokemonNames.take(2).map {
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/0.png"
        // ↑ 포켓몬 ID 0으로 하드코딩 = 물음표 스프라이트
    },
```

**영향**:
- 앱이 고장난 것처럼 보임
- 사용자가 대표 포켓몬으로 덱을 식별할 수 없음
- 핵심 UX 기능이 완전히 작동 안 함

**해결 방안**:
```kotlin
// 옵션 1: 포켓몬 이름 → PokeAPI ID 매핑 (간단, 일반적인 포켓몬에서 작동)
representativePokemonImageUrls =
    pokemonNames.take(2).map { name ->
        val pokemonId = mapPokemonNameToId(name)  // "Mewtwo" → 150
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pokemonId.png"
    }

// 옵션 2: Limitless API icons 필드 사용 (DeckResponse에 이미 있음)
// PR #41이 준비해둔 것 - icons 필드가 이제 올바르게 매핑됨
```

**권장 해결책**: 옵션 2 (`DeckResponse.icons` 필드 사용)
- API가 이미 아이콘 URL 제공
- 이름→ID 매핑 로직 불필요
- `Standing.deck.icons`에서 이미 사용 가능

**수정할 파일**:
1. `DefaultDecksRepo.kt:28-31` - 하드코딩 대신 `Standing.deck.icons` 사용
2. 아이콘 없는 덱에 대한 null 안전성 처리 추가

**테스트 영향**:
- `FakeDecksRepo`를 실제와 유사한 포켓몬 이미지 URL로 업데이트
- 아이콘 URL이 올바르게 전파되는지 확인하는 관련 테스트 업데이트

---

### 🔴 문제 5: 가짜 덱 설명 (심각한 버그)

**상태**: **즉시 수정 필요** 🚨

**원인** (`DeckItem.kt:147`):
```kotlin
Text(text = FAKE_TIER_DECK_DESCRIPTION)
// DeckItem에 전달된 'description' 파라미터를 무시!
```

**영향**:
- 모든 덱이 동일한 가짜 뮤츠 설명 표시
- 플레이스홀더 데이터가 있는 프로토타입처럼 보임
- 사용자 신뢰 위반 - 비전문적으로 보임

**해결 방안**:
```kotlin
// 옵션 1: 설명 완전히 제거 (API가 제공하지 않는 경우)
// 설명을 전혀 표시하지 않음 - 가짜 데이터보다 깔끔함

// 옵션 2: API의 실제 설명 표시 (가능한 경우)
Text(text = description.ifEmpty { "설명 없음" })

// 옵션 3: 대신 덱 구성 요약 표시
Text(text = "포함: ${pokemonNames.joinToString(", ")}")
```

**권장 해결책**: 옵션 1 (설명 제거)
- API가 덱 설명 제공하지 않음
- `DefaultDecksRepo.kt:44`에서 `description = ""`로 설정
- 가짜 데이터보다 설명 없는 게 나음

**수정할 파일**:
1. `DeckItem.kt:147` - 설명 Text 제거 또는 조건부로 숨김
2. 빈 설명을 우아하게 처리하도록 UI 업데이트
3. 더 이상 사용하지 않으면 `FAKE_TIER_DECK_DESCRIPTION` 상수 제거

**테스트 영향**:
- 프리뷰 함수가 가짜 설명에 의존하지 않도록 업데이트
- 빈 설명으로 UI가 작동하는지 확인

---

### 🟡 문제 2: 덱 이름이 잘림 (UI 개선)

**상태**: **수정 권장** (중간 우선순위)

**원인** (`DeckItem.kt:199-205`):
```kotlin
Text(
    text = deckName,
    style = MaterialTheme.typography.titleMedium,
    maxLines = 1,
    overflow = TextOverflow.Ellipsis,
)
```

**영향**:
- "Pikachu ex Zebstrika" 같은 덱 이름이 "Pikachu ex Zeb..."로 표시
- 가독성 저하, 특히 여러 포켓몬이 있는 덱의 경우

**해결 방안**:
```kotlin
// 옵션 1: maxLines를 2로 증가
Text(
    text = deckName,
    style = MaterialTheme.typography.titleMedium,
    maxLines = 2,  // 두 번째 줄로 줄바꿈 허용
    overflow = TextOverflow.Ellipsis,
)

// 옵션 2: 더 많은 텍스트를 맞추기 위해 더 작은 글꼴 크기 사용
Text(
    text = deckName,
    style = MaterialTheme.typography.titleSmall,  // 더 작은 글꼴
    maxLines = 1,
    overflow = TextOverflow.Ellipsis,
)

// 옵션 3: 콘텐츠 길이에 따라 텍스트 자동 크기 조정
```

**권장 해결책**: 옵션 1 (maxLines = 2)
- 간단하고 효과적
- 가독성 유지
- 리스트 UI의 일반적인 패턴

**수정할 파일**:
1. `DeckItem.kt:199-205` - `maxLines = 1`을 `maxLines = 2`로 변경

**테스트 영향**: 최소 - UI 테스트가 여전히 통과하는지 확인

---

### 🟢 문제 4: Cost 필드가 항상 0 표시 (이미 추적됨)

**상태**: **이슈 #36으로 연기** ✅

**원인** (`DefaultDecksRepo.kt:39`):
```kotlin
detail = DeckDetailInformation(
    cost = 0,  // TODO: 실제 cost는 카드 데이터에서. 이슈 #36 참조
```

**발견 사항**: 이미 이슈 #36 "DeckDetailInformation을 위한 카드 데이터 통합 구현"에서 추적 중

**해결책**:
- 이슈 #36에서 카드 데이터 저장소 통합 구현 예정
- 개별 카드 비용에서 덱 비용 계산
- 즉시 조치 불필요

**결정**: 이슈 #36을 백로그에 유지, DeckDetailScreen 이후 구현

---

### 🔍 문제 6: 추가 이슈 분석

**스크린샷 분석 결과**:

1. **간격/레이아웃**:
   - 덱 아이템이 적절히 간격 유지 ✅
   - Win/share rate 레이아웃이 명확 ✅
   - 명백한 레이아웃 이슈 없음

2. **시각적 계층구조**:
   - 포켓몬 이미지가 두드러짐 (수정되면)
   - 덱 이름이 명확함 (잘림 수정되면)
   - 통계가 읽기 쉬움 ✅

3. **누락된 기능** (버그 아님, 고려사항):
   - 로딩 인디케이터 보이지 않음 (UiState를 통해 이미 구현됨)
   - 에러 상태 보이지 않음 (UiState를 통해 이미 구현됨)
   - 빈 상태 보이지 않음 (덱이 없으면 표시됨)

**결론**: 추가 심각한 이슈 없음. 문제 1, 5, 2에 집중.

---

## 🎯 우선순위 결정 매트릭스

| 문제 | 심각도 | 사용자 영향 | 수정 노력 | 우선순위 | 조치 |
|------|--------|------------|----------|---------|------|
| 1. 포켓몬 이미지 | 🔴 심각 | 높음 - 핵심 기능 고장 | 중간 | **P0** | 즉시 수정 |
| 5. 가짜 설명 | 🔴 심각 | 높음 - 비전문적 | 낮음 | **P0** | 즉시 수정 |
| 2. 이름 잘림 | 🟡 중간 | 중간 - 가독성 이슈 | 낮음 | **P1** | 즉시 수정 |
| 3. Win/Share Rate | ✅ 작동 | 없음 - 버그 아님 | 없음 | **없음** | 조치 없음 |
| 4. Cost = 0 | 🟢 낮음 | 낮음 - #36에서 추적 | 높음 | **P2** | #36으로 연기 |
| 6. 추가 이슈 | ✅ 없음 | 없음 | 없음 | **없음** | 조치 없음 |

---

## 📋 권장 계획

### Phase 1: 심각한 버그 수정 (P0) - 먼저 수행 🚨

**브랜치**: `fix/tierdecks-ui-critical-bugs`

**생성할 이슈**:
- **새 이슈**: "TierDecksScreen에서 포켓몬 이미지 URL 수정" (P0, MVP 차단)
- **새 이슈**: "DeckItem에서 가짜 덱 설명 제거" (P0, MVP 차단)

**구현 순서**:
1. 문제 1 수정 (포켓몬 이미지) - API의 `Standing.deck.icons` 사용
2. 문제 5 수정 (가짜 설명) - 설명 표시 완전히 제거
3. 빌드 & 테스트
4. 커밋: `fix(ui): resolve Pokemon images and fake descriptions in TierDecksScreen`

**예상 시간**: 1-2시간

---

### Phase 2: UI 개선 (P1) - 두 번째 수행

**브랜치**: Phase 1과 동일 또는 새 `fix/tierdecks-name-truncation`

**구현**:
1. 문제 2 수정 (이름 잘림) - `maxLines = 2`로 변경
2. 빌드 & 테스트
3. 커밋: `fix(ui): improve deck name readability in DeckItem`

**예상 시간**: 30분

---

### Phase 3: 기존 로드맵 재개 (P2)

**이슈 #38-40 계속**:
- #38: DeckDetailScreen UI 구현
- #39: DeckDetailViewModel 구현
- #40: TierDecksScreen에서 DeckDetailScreen으로 네비게이션 추가

**그 다음 이슈 #36** (DeckDetailScreen 완료 후):
- #36: DeckDetailInformation을 위한 카드 데이터 통합 구현

---

## 🔄 기존 이슈에 대한 영향

### 우선순위 낮출 이슈 (없음)
모든 기존 이슈는 유효하고 올바른 우선순위 순서 유지.

### 업데이트할 이슈
- **이슈 #36**: TierDecksScreen에서 cost 필드가 현재 0 표시 중이라는 노트 추가 (카드 데이터 통합까지 허용 가능)

### 생성할 이슈
1. **새 이슈**: "TierDecksScreen에서 포켓몬 이미지 URL 수정"
   - **라벨**: `bug`, `ui`, `priority:critical`, `tier-decks`
   - **마일스톤**: MVP Phase 1
   - **차단**: 사용자 테스트, 앱 신뢰도
   - **설명**: 하드코딩된 ID 0으로 인해 포켓몬 이미지가 물음표 표시. API의 `Standing.deck.icons` 사용.

2. **새 이슈**: "DeckItem에서 가짜 덱 설명 제거"
   - **라벨**: `bug`, `ui`, `priority:critical`, `tier-decks`
   - **마일스톤**: MVP Phase 1
   - **차단**: 사용자 테스트, 앱 신뢰도
   - **설명**: DeckItem이 하드코딩된 가짜 뮤츠 설명 표시. 제거하거나 실제 데이터 사용.

3. **새 이슈**: "TierDecksScreen에서 덱 이름 가독성 향상"
   - **라벨**: `enhancement`, `ui`, `priority:medium`, `tier-decks`
   - **마일스톤**: MVP Phase 1
   - **설명**: 덱 이름이 몇 글자 후 잘림. `maxLines`를 2로 증가.

---

## 📁 수정할 파일

### Phase 1: 심각한 버그

**app/src/main/java/tcg/pocket/dex/repo/decks/DefaultDecksRepo.kt**
```kotlin
// 28-31줄: 수정 전
representativePokemonImageUrls =
    pokemonNames.take(2).map {
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/0.png"
    },

// 28-31줄: 수정 후
representativePokemonImageUrls =
    standing.deck.icons.take(2),  // API의 실제 아이콘 URL 사용
```

**app/src/main/java/tcg/pocket/dex/component/DeckItem.kt**
```kotlin
// 147줄: 수정 전
Text(text = FAKE_TIER_DECK_DESCRIPTION)

// 147줄: 수정 후
// 설명 Text 완전히 제거, 또는:
if (description.isNotEmpty()) {
    Text(text = description)
}
```

### Phase 2: UI 개선

**app/src/main/java/tcg/pocket/dex/component/DeckItem.kt**
```kotlin
// 199-205줄: 수정 전
Text(
    text = deckName,
    style = MaterialTheme.typography.titleMedium,
    maxLines = 1,
    overflow = TextOverflow.Ellipsis,
)

// 199-205줄: 수정 후
Text(
    text = deckName,
    style = MaterialTheme.typography.titleMedium,
    maxLines = 2,  // 줄바꿈 허용
    overflow = TextOverflow.Ellipsis,
)
```

---

## ✅ 성공 기준

### Phase 1 완료 조건:
- [ ] 포켓몬 이미지가 실제 포켓몬 스프라이트 표시 (물음표 아님)
- [ ] 덱 설명이 더 이상 가짜 뮤츠 텍스트 표시 안 함
- [ ] `./gradlew build` 통과
- [ ] `./gradlew test` 통과 (모든 테스트 업데이트됨)
- [ ] 시각적 확인: TierDecksScreen이 전문적으로 보임

### Phase 2 완료 조건:
- [ ] 덱 이름이 완전히 읽힘 (또는 적절한 길이에서 잘림)
- [ ] UI가 깔끔하고 어수선하지 않음
- [ ] 모든 품질 게이트 통과

### 전체 성공:
- [ ] TierDecksScreen이 사용자 테스트를 위한 MVP 준비 완료
- [ ] 사용자에게 표시되는 가짜/플레이스홀더 데이터 없음
- [ ] 앱이 전문적이고 신뢰할 수 있게 보임

---

## 🚀 GitHub 이슈 관리 계획

### Step 1: 새 이슈 생성
```bash
gh issue create --title "TierDecksScreen에서 포켓몬 이미지 URL 수정" \
  --label "bug,ui,priority:critical,tier-decks" \
  --body "DefaultDecksRepo.kt:28-31에서 하드코딩된 ID 0으로 인해 포켓몬 이미지가 물음표 표시. API의 Standing.deck.icons 대신 사용."

gh issue create --title "DeckItem에서 가짜 덱 설명 제거" \
  --label "bug,ui,priority:critical,tier-decks" \
  --body "DeckItem.kt:147이 하드코딩된 FAKE_TIER_DECK_DESCRIPTION 표시. 설명 표시 제거하거나 API의 실제 데이터 사용."

gh issue create --title "TierDecksScreen에서 덱 이름 가독성 향상" \
  --label "enhancement,ui,priority:medium,tier-decks" \
  --body "DeckItem.kt:199-205에서 덱 이름 잘림. 가독성 향상을 위해 maxLines를 1에서 2로 변경."
```

### Step 2: 기존 이슈 업데이트
```bash
# 이슈 #36에 코멘트 추가
gh issue comment 36 --body "참고: TierDecksScreen에서 Cost 필드가 현재 0 표시 (DefaultDecksRepo.kt:39). 카드 데이터 통합 구현까지 허용 가능."
```

### Step 3: 이슈 목록 확인
```bash
gh issue list --state open
# 다음이 표시되어야 함:
# - 새로운 심각한 UI 버그 (포켓몬 이미지, 가짜 설명)
# - 새로운 UI 개선 (이름 잘림)
# - 기존 이슈 #36, #38, #39, #40 (우선순위 변경 없음)
```

---

## 📊 업데이트된 MVP 로드맵

**Phase 1: TierDecksScreen (진행 중 - 85% → 95%)**
- ✅ DefaultDecksRepo 구현 (PR #41)
- ✅ API 통합 (PR #41)
- 🔄 **심각한 UI 수정** (포켓몬 이미지, 가짜 설명) ← 새로 추가
- 🔄 **UI 개선** (이름 잘림) ← 새로 추가
- ⏳ 이슈 #36: 카드 데이터 통합 (Phase 3으로 연기)

**Phase 2: DeckDetailScreen (다음 - 0%)**
- 이슈 #38: DeckDetailScreen UI 구현
- 이슈 #39: DeckDetailViewModel 구현
- 이슈 #40: TierDecksScreen에서 네비게이션 추가

**Phase 3: 카드 데이터 통합 (나중에)**
- 이슈 #36: 비용 계산을 위한 카드 데이터 통합 구현

---

## 🎯 권장사항

**이 계획 승인** 시:
1. 앱이 고장난 것처럼 보이게 하는 심각한 사용자 대면 버그 수정
2. 가독성 향상을 위한 UI 개선
3. 자신 있게 DeckDetailScreen 구현 재개

**총 지연**: ~2-3시간 (로드맵에 최소 영향)
**이점**: 사용자 테스트를 위한 전문적이고 신뢰할 수 있는 MVP

---

## 📝 구현 체크리스트

### 시작 전
- [ ] 사용자가 이 계획 승인
- [ ] 피처 브랜치 생성: `fix/tierdecks-ui-critical-bugs`
- [ ] local dev가 origin/dev와 동기화되었는지 확인

### Phase 1: 심각한 버그
- [ ] 포켓몬 이미지 수정 구현 (`Standing.deck.icons` 사용)
- [ ] 테스트 업데이트 (FakeDecksRepo, 프리뷰 함수)
- [ ] 가짜 설명 수정 구현 (제거 또는 조건부 표시)
- [ ] `./gradlew build` 실행 (병렬)
- [ ] `./gradlew test` 실행 (병렬)
- [ ] 에뮬레이터/디바이스에서 시각적 확인
- [ ] 사용자 승인으로 커밋
- [ ] 추적을 위한 GitHub 이슈 생성

### Phase 2: UI 개선
- [ ] 이름 잘림 수정 구현 (`maxLines = 2`)
- [ ] `./gradlew build` 실행
- [ ] 시각적 확인
- [ ] 사용자 승인으로 커밋

### Phase 3: 마무리
- [ ] origin에 브랜치 푸시
- [ ] dev로 PR 생성
- [ ] `.claude/plan/mvp-progress.md` 업데이트
- [ ] 이슈 #38 (DeckDetailScreen) 재개
