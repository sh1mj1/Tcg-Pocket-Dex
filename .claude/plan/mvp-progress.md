# TcgPocketDex MVP 출시 계획

**작성일**: 2025-12-09
**목표**: 1개월 내 MVP 출시 (2-4일 집중 작업)
**현재 완성도**: 60%

---

## 📊 현재 상태 분석

### 완성된 기능 (40%)
- ✅ **AllCardsScreen**: TCGdex API 연동, 카드 그리드 표시, CardDetail 네비게이션
- ✅ **TierDecksScreen**: UI 완성 (단, FakeDecksRepo 사용 중)
- ✅ **CardDetailScreen**: 카드 상세 정보, 포켓몬 기술, Related Decks 표시

### 부분 구현 (30%)
- 🟡 **TierDecksScreen**: UI는 완성, API 연동 필요 (현재 가짜 데이터)
- 🟡 **DeckDetailScreen**: 라우팅만 정의, UI/API 미구현
- 🟡 **에러 처리**: 일부 화면만 처리, 시스템 전체 적용 필요

### 미구현 (30%)
- ❌ **ExpansionPacksScreen**: 텍스트만 표시
- ❌ **SearchScreen**: 3가지 타입 모두 와이어프레임
- ❌ **SettingScreen**: 텍스트만 표시

---

## 🎯 MVP 범위 정의

### ✅ MVP 포함 기능

1. **티어 덱 목록** (TierDecks 탭)
   - Limitless TCG API 실제 연동
   - 순위, 덱 이름, 승률, 점유율 표시
   - 확장/축소 기능

2. **티어 덱 상세** (DeckDetailScreen)
   - 덱 구성 카드 목록
   - 덱 통계 (순위, 승률, 점유율)
   - 카드 클릭 → CardDetail 이동

3. **모든 카드 목록** (AllCards 탭)
   - TCGdex API 연동 ✅
   - 카드 그리드 레이아웃 ✅

4. **카드 상세** (CardDetail 화면)
   - 카드 이미지 및 메타데이터 ✅
   - 포켓몬 기술 정보 ✅
   - 관련 덱 목록 ✅

5. **에러 처리 & 로딩 상태**
   - 네트워크 에러 알림
   - CircularProgressIndicator

### ❌ MVP 제외 기능

- **ExpansionPacks 탭**: Bottom bar에서 제거
- **Search 기능**: Top bar 검색 아이콘 제거
- **Setting 화면**: Top bar 설정 아이콘 제거
- **Related Cards**: API 미지원 (이미 주석 처리됨)
- **즐겨찾기, 덱 빌더, 사용자 계정**: 미구현

---

## 📋 Phase별 작업 계획

## Phase 1: 핵심 기능 완성 (14시간)

### 1.1 티어 덱 실제 API 연동 ⏱️ 6시간

**현재 상태**: `FakeDecksRepo`만 존재, UI는 완성

**작업 목록**:
- [ ] `DefaultDecksRepo` 클래스 생성
  - 위치: `app/src/main/java/tcg/pocket/dex/repo/decks/`
  - 인터페이스: `DecksRepo` 구현

- [x] `RemoteTournamentDataSource` 연결 ✅ (Issues #24-25 완료)
  - 위치: `app/src/main/java/tcg/pocket/dex/datasource/`
  - API: Limitless TCG 토너먼트 데이터

- [x] 덱 통계 집계 로직 구현 (README 3-4단계) ✅ (Issues #26-27 완료)
  - 토너먼트 결과 → 덱별 집계
  - 승률 계산 (승/전체)
  - 점유율 계산 (덱 사용 횟수/전체)
  - 참고: `DeckStatsAggregatorTest.kt`

- [x] `TournamentStatsRepo` 생성 ✅ (Issue #28 완료)
  - 위치: `app/src/main/java/tcg/pocket/dex/repo/tournamentstats/`
  - 전체 파이프라인 오케스트레이션 레이어
  - `getDeckStatistics()` → `List<CalculatedDeck>` 반환

- [ ] `TierDecksViewModel` 수정
  - `FakeDecksRepo` → `DefaultDecksRepo` 교체
  - 에러 처리 추가

- [x] 테스트 작성 ✅ (Issue #28 완료)
  - `DefaultTournamentStatsRepoTest.kt` (16 테스트)
  - API 연동 검증

**완료 조건**:
- TierDecksScreen에 실제 덱 데이터 표시
- 순위, 승률, 점유율 정확히 계산됨

---

### 1.2 덱 상세 화면 완성 ⏱️ 4시간

**현재 상태**: "Deck Detail id: {deckId}" 텍스트만 표시

**작업 목록**:
- [ ] `DeckDetailViewModel` 데이터 로딩
  - `DecksRepo.deckDetail(id)` API 호출
  - 덱 정보, 구성 카드 목록 로드

- [ ] `DeckDetailScreen` UI 구현
  ```
  Layout:
  ├─ 덱 이름 (타이틀)
  ├─ 통계 섹션 (순위, 승률, 점유율)
  ├─ 덱 구성 카드 목록 (LazyVerticalGrid)
  └─ 덱 설명/전략 (선택)
  ```

- [ ] 카드 클릭 네비게이션
  - 카드 클릭 → `CardDetailScreen` 이동
  - 카드 ID 전달

- [ ] 로딩/에러 상태 처리
  - CircularProgressIndicator
  - 에러 메시지 표시

**완료 조건**:
- 덱 클릭 시 상세 정보 정상 표시
- 덱 구성 카드 그리드로 표시
- 카드 클릭 시 상세 화면 이동

---

### 1.3 에러 처리 시스템 구축 ⏱️ 3시간

**현재 상태**: 명시적 에러 처리 없음, 일부 화면만 로딩 표시

**작업 목록**:
- [ ] `UiState` sealed interface 생성
  ```kotlin
  // 위치: app/src/main/java/tcg/pocket/dex/common/UiState.kt
  sealed interface UiState<out T> {
      data object Loading : UiState<Nothing>
      data class Success<T>(val data: T) : UiState<T>
      data class Error(val message: String) : UiState<Nothing>
  }
  ```

- [ ] 모든 ViewModel에 에러 처리 추가
  - `AllCardsViewModel`: try-catch 추가
  - `CardDetailViewModel`: try-catch 추가
  - `TierDecksViewModel`: try-catch 추가
  - `DeckDetailViewModel`: try-catch 추가

- [ ] 에러 UI 컴포넌트 생성
  ```kotlin
  @Composable
  fun ErrorMessage(
      message: String,
      onRetry: () -> Unit
  )
  ```

- [ ] Snackbar 또는 Dialog로 에러 알림
  - 네트워크 에러: "네트워크 연결을 확인해주세요"
  - API 에러: "데이터를 불러올 수 없습니다"

**완료 조건**:
- 네트워크 끊고 앱 실행 시 에러 메시지 표시
- "재시도" 버튼 동작
- 모든 화면에 에러 처리 적용

---

### 1.4 로딩 인디케이터 추가 ⏱️ 1시간

**작업 목록**:
- [ ] `AllCardsViewModel` 수정
  - `cardsState`를 `StateFlow<UiState<List<CardData>>>` 변경

- [ ] `AllCardsScreen` 로딩 UI
  ```kotlin
  when (val state = cardsState) {
      is UiState.Loading -> CircularProgressIndicator()
      is UiState.Success -> CardGrid(state.data)
      is UiState.Error -> ErrorMessage(state.message)
  }
  ```

- [ ] `TierDecksScreen` 동일 패턴 적용

**완료 조건**:
- 앱 시작 시 로딩 인디케이터 표시
- 데이터 로드 완료 시 자동으로 사라짐

---

## Phase 2: UI 정리 & 개선 (1.5시간)

### 2.1 미구현 기능 숨기기 ⏱️ 30분

**작업 목록**:
- [ ] Bottom Bar에서 `ExpansionPacks` 제거
  ```kotlin
  // navigation/PocketDexDestination.kt
  val bottomBarScreens = listOf(AllCards, TierDecks)
  // ExpansionPacks 제거
  ```

- [ ] Top Bar 검색 아이콘 제거
  ```kotlin
  // PocketDexApp.kt - PocketDexTopBar
  // onSearchClicked 제거
  ```

- [ ] Top Bar 설정 아이콘 제거
  ```kotlin
  // PocketDexApp.kt - PocketDexTopBar
  // onSettingClicked 제거
  ```

- [ ] Related Cards 관련 코드 정리
  - `CardDetailViewModel.relatedCardsState` 제거 또는 주석
  - `RelatedCardsSection.kt` 파일 삭제

**완료 조건**:
- Bottom bar 2개 탭만 표시 (TierDecks, AllCards)
- Top bar 아이콘 없음 (타이틀만)

---

### 2.2 앱 아이콘 교체 ⏱️ 1시간

**작업 목록**:
- [ ] 앱 아이콘 디자인
  - 포켓몬 카드 또는 덱 이미지 활용
  - 512x512px PNG

- [ ] 모든 density 생성
  - Android Asset Studio 사용
  - mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi

- [ ] `mipmap-*` 폴더에 배치
  - `ic_launcher.webp` 교체
  - `ic_launcher_round.webp` 교체

**완료 조건**:
- 홈 화면에 새 아이콘 표시

---

## Phase 3: 배포 준비 (3시간)

### 3.1 ProGuard 설정 ⏱️ 1시간

**작업 목록**:
- [ ] `build.gradle.kts` 수정
  ```kotlin
  buildTypes {
      release {
          isMinifyEnabled = true
          proguardFiles(...)
      }
  }
  ```

- [ ] ProGuard rules 추가
  ```proguard
  # Ktor
  -keep class io.ktor.** { *; }

  # Kotlinx Serialization
  -keepattributes *Annotation*, InnerClasses
  -dontnote kotlinx.serialization.**
  -keep,includedescriptorclasses class tcg.pocket.dex.**$$serializer { *; }
  ```

- [ ] Release 빌드 테스트
  ```bash
  ./gradlew assembleRelease
  ```

**완료 조건**:
- Release APK 빌드 성공
- 앱 실행 및 모든 기능 동작 확인

---

### 3.2 서명 설정 ⏱️ 30분

**작업 목록**:
- [ ] Keystore 생성
  ```bash
  keytool -genkey -v -keystore tcgpocketdex.jks \
    -keyalg RSA -keysize 2048 -validity 10000 \
    -alias tcgpocketdex
  ```

- [ ] `build.gradle.kts`에 signingConfigs 추가
  ```kotlin
  signingConfigs {
      create("release") {
          storeFile = file("../tcgpocketdex.jks")
          storePassword = System.getenv("KEYSTORE_PASSWORD")
          keyAlias = "tcgpocketdex"
          keyPassword = System.getenv("KEY_PASSWORD")
      }
  }
  ```

- [ ] GitHub Secrets 설정 (CI용)

**완료 조건**:
- 서명된 Release APK 생성

---

### 3.3 문서 작성 ⏱️ 1.5시간

**작업 목록**:
- [ ] README 업데이트
  - 앱 소개 (한글/영문)
  - 주요 기능
  - 스크린샷 4-6장

- [ ] Play Store 설명 작성
  - 짧은 설명 (80자)
  - 긴 설명 (4000자)
  - 키워드

- [ ] 스크린샷 촬영
  - TierDecksScreen
  - DeckDetailScreen
  - AllCardsScreen
  - CardDetailScreen

**완료 조건**:
- README에 스크린샷 포함
- Play Store 설명 준비 완료

---

## Phase 4: 품질 보증 (3시간)

### 4.1 테스트 실행 ⏱️ 1시간

**작업 목록**:
- [ ] ktlint 통과
  ```bash
  ./gradlew ktlintCheck
  ./gradlew ktlintFormat  # 자동 수정
  ```

- [ ] Unit tests 통과
  ```bash
  ./gradlew test
  ```

- [ ] Instrumented tests 통과
  ```bash
  ./gradlew connectedAndroidTest
  ```

**완료 조건**:
- 모든 테스트 통과
- ktlint 위반 사항 없음

---

### 4.2 최종 검증 ⏱️ 2시간

**작업 목록**:
- [ ] 네비게이션 플로우 테스트
  - TierDecks → DeckDetail → CardDetail
  - AllCards → CardDetail
  - Back 버튼 동작

- [ ] 네트워크 에러 시나리오
  - 비행기 모드 → 앱 실행 → 에러 메시지 확인
  - 재시도 버튼 동작 확인

- [ ] 메모리 누수 확인
  - Android Profiler 사용
  - 화면 전환 반복

- [ ] 실제 기기 테스트
  - 최소 3개 기기 (다양한 화면 크기)

**완료 조건**:
- 모든 시나리오 통과
- 크래시 없음

---

## 🗓️ 권장 타임라인

### Day 1 (8시간) - 핵심 기능
- **09:00-13:00** (4h): 티어 덱 API 연동
  - DefaultDecksRepo 구현
  - RemoteTournamentDataSource 연결

- **14:00-18:00** (4h): 덱 상세 화면
  - DeckDetailViewModel 데이터 로딩
  - DeckDetailScreen UI 구현

### Day 2 (8시간) - 에러 처리 & UI 정리
- **09:00-12:00** (3h): 에러 처리 시스템
  - UiState 구현
  - 모든 ViewModel 적용

- **13:00-14:00** (1h): 로딩 인디케이터

- **14:00-15:30** (1.5h): UI 정리
  - 미구현 기능 숨기기
  - 앱 아이콘 교체

- **15:30-18:00** (2.5h): 배포 준비 시작
  - ProGuard 설정

### Day 3 (5.5시간) - 배포 & 테스트
- **09:00-10:30** (1.5h): 서명 설정 + 문서 작성

- **10:30-13:00** (2.5h): 테스트 실행

- **14:00-15:30** (1.5h): 최종 검증 및 버그 수정

### Day 4 (예비)
- 추가 버그 수정
- Play Store 준비

---

## ✅ 체크리스트

### Phase 1: 핵심 기능
- [x] TournamentStatsRepo 구현 완료 ✅ (Issue #28 - 2025-12-10)
- [ ] DefaultDecksRepo 구현 완료 (TournamentStatsRepo 사용)
- [ ] 티어 덱 실제 데이터 표시
- [ ] DeckDetailScreen UI 완성
- [ ] 모든 ViewModel 에러 처리
- [ ] 모든 화면 로딩 인디케이터

### Phase 2: UI 정리
- [ ] Bottom bar 2개 탭만 표시
- [ ] Top bar 검색/설정 아이콘 제거
- [ ] 앱 아이콘 교체

### Phase 3: 배포 준비
- [ ] ProGuard 활성화 및 테스트
- [ ] Keystore 생성 및 서명 설정
- [ ] README 스크린샷 추가
- [ ] Play Store 설명 작성

### Phase 4: 품질 보증
- [ ] ktlint 통과
- [ ] 모든 테스트 통과
- [ ] 네트워크 에러 시나리오 검증
- [ ] 실제 기기 3대 이상 테스트

### 배포
- [ ] Release APK 빌드 성공
- [ ] 서명된 APK 생성
- [ ] Play Console 등록
- [ ] 스크린샷 업로드
- [ ] 앱 설명 입력
- [ ] AAB 업로드

---

## ⚠️ 위험 요소 및 대응

### 위험 1: Limitless TCG API 불안정
- **확률**: 중
- **영향**: 치명적
- **대응**:
  - API 테스트 먼저 수행
  - 캐싱 전략 수립
  - 에러 처리 강화

### 위험 2: 덱 통계 집계 로직 복잡도
- **확률**: 높음
- **영향**: 높음
- **대응**:
  - README 6단계 참고
  - 기존 테스트 활용 (`DeckStatsAggregatorTest.kt`)
  - 단계별 구현 및 검증

### 위험 3: ProGuard 설정 실패
- **확률**: 중
- **영향**: 중
- **대응**:
  - 단계적 활성화
  - Ktor, Kotlinx.serialization 공식 규칙 참고
  - Release 빌드 조기 테스트

### 위험 4: 시간 부족
- **확률**: 높음
- **영향**: 중
- **대응**:
  - MVP 범위 엄격히 준수
  - Nice-to-have 기능 과감히 포기
  - 단계별 배포 고려

---

## 📝 참고 사항

### MVP 출시 후 고려 사항
- Related Cards 기능 (API 지원 시)
- 확장팩별 필터링
- 검색 기능
- 설정 화면 (다크 모드, 언어)
- 즐겨찾기
- 덱 빌더

### 프로젝트 유기 대비
```bash
# MVP 출시 시점에 안정 브랜치 생성
git checkout -b release/v1.0-mvp
git tag v1.0-mvp
git push origin release/v1.0-mvp
git push origin v1.0-mvp
```

---

## 🎯 최종 목표

**MVP 출시 기준**:
- ✅ 티어 덱 조회 가능
- ✅ 덱 상세 및 구성 카드 확인 가능
- ✅ 카드 상세 정보 확인 가능
- ✅ 에러 처리 및 로딩 상태 표시
- ✅ Play Store 출시 가능 상태

**예상 완료**: 2-4일 (21.5시간)
