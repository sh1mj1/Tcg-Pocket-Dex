---
description: Solve a GitHub issue end-to-end with automated testing, validation, and PR creation
---

# Elite GitHub Issue Resolver

You are now an elite software developer specialized in solving GitHub issues systematically and thoroughly.
You receive **the GitHub issue number**, **the base branch**, and **the PR language** as input arguments, and solve the issue completely from analysis to PR creation.

## 🎯 Input Arguments

- `--issue` (required): GitHub issue number (e.g., `24`, `72`)
- `--base` (optional): Base branch to branch off from (`main` | `dev`). Default: `main`
- `--lang` (optional): Language for PR creation (`ko` | `en`). Default: `ko`

## 📋 Workflow Steps

### Step 1: Fetch and Understand the Issue

1. Use GitHub CLI to fetch the issue details:
   ```bash
   gh issue view <issue_number>
   ```
2. Parse and understand:
   - Title
   - Description/Body
   - Labels (type, area, complexity)
   - Dependencies mentioned
   - Acceptance criteria

### Step 2: Branch Setup

1. Ensure you're on the base branch specified by `--base` argument
2. Pull latest changes from remote
3. Generate a descriptive branch name:
   - Extract the issue type from labels (feature, bugfix, refactor, etc.)
   - Convert the issue title to kebab-case for the descriptive name
   - Format: `<type>/<descriptive-name>` (e.g., `feature/tournament-standings`, `bugfix/fix-card-display`)
   - If no type label exists, use format: `issue-<ISSUE_NUMBER>-<descriptive-name>`
4. Create and switch to the new branch

**Commands:**
```bash
git checkout <BASE_BRANCH>
git pull origin <BASE_BRANCH>
git checkout -b <BRANCH_NAME>
```

Where:
- `<BASE_BRANCH>` is the value from `--base` argument (default: `main`)
- `<BRANCH_NAME>` is the generated descriptive branch name

### Step 3: Codebase Analysis

**CRITICAL: Use parallel research agents for efficiency**

1. Launch **up to 10 independent research agents** in parallel using the Task tool with `subagent_type=Explore`
2. Each agent should research different aspects:
   - Existing patterns for similar features
   - Files mentioned in the issue's "Implementation Notes"
   - Related domain models
   - Repository patterns
   - Service implementations
   - Test patterns
   - Error handling approaches
   - Dependency injection patterns
   - Any related existing functionality

3. **Launch all research agents in a SINGLE message** with multiple Task tool calls
4. Gather context efficiently without redundant searches

### Step 4: Plan the Solution

1. Use a scratchpad (text output) to show your thinking process
2. Synthesize information from all research agents
3. Create a detailed implementation plan:
   - Files to create/modify
   - Code patterns to follow
   - Dependencies between changes
   - Potential edge cases
   - Testing strategy

### Step 5: Implement the Solution

1. Launch **ONE Task agent** with `subagent_type=general-purpose` to implement the solution
2. Provide the agent with:
   - Complete context from research
   - Detailed implementation plan
   - Acceptance criteria from the issue
   - Instruction to follow existing code patterns

### Step 5.5: User Review and Commit Implementation

**CRITICAL: MUST get user confirmation before committing**

**This step follows `.claude/rules/git-workflow.md` rules strictly**

1. **Show implementation summary to user**:
   ```
   📊 Implementation Complete

   Changed files:
   - app/src/.../DefaultDecksRepo.kt (new, 120 lines)
   - app/src/.../TierDecksViewModel.kt (modified, +15/-3)

   Build: ✅ SUCCESS
   Tests: ✅ 15/15 passed

   Proposed commit message:
   feat(repo): add DefaultDecksRepo implementation

   - Implement DecksRepo interface
   - Connect RemoteTournamentDataSource
   - Add deck statistics aggregation logic

   Refs #<ISSUE_NUMBER>
   ```

2. **Request user decision**:
   ```
   Proceed with commit?
   - Type "yes" to commit
   - Type "no" to skip
   - Type "modify" to change commit message
   ```

3. **Only after user approval**:
   ```bash
   git add .
   git commit -m "<type>(<scope>): <subject>

   <body>

   Refs #<ISSUE_NUMBER>"
   ```

**IMPORTANT**:
- ⛔ DO NOT auto-commit without user approval
- ✅ Follow `.claude/rules/git-workflow.md` rules strictly
- ✅ Show all changes before committing
- ✅ User must explicitly approve

**Commit Message Convention (Angular Style):**

Follow the Angular Git commit convention: `<type>(<scope>): <subject>`

**Type (Required):**
- `feat`: New feature
- `fix`: Bug fix
- `refactor`: Code change that neither fixes a bug nor adds a feature
- `perf`: Performance improvement
- `test`: Adding or updating tests
- `docs`: Documentation changes
- `style`: Code style changes (formatting, missing semicolons, etc.)
- `chore`: Regular maintenance (updating dependencies, etc.)
- `build`: Build system or external dependency changes
- `ci`: CI configuration changes

**Scope (Optional):**
- The module or area affected (e.g., `api`, `ui`, `repo`, `service`)

**Subject (Required):**
- Start with lowercase
- Use imperative mood ("add" not "added" or "adds")
- No period at the end
- Keep under 50 characters

**Body (Optional):**
- Separate from header with blank line
- Explain motivation and contrast with previous behavior
- Max 72 characters per line

**Footer (Optional):**
- Reference issues: `Refs #123`, `Closes #123`, `Fixes #123`
- Breaking changes: `BREAKING CHANGE: description`

**Example:**
```bash
git commit -m "feat(api): add tournament standings endpoint

Implement new GET endpoint to fetch tournament standings data.
Returns list of tournaments with participant rankings.

Refs #72"
```

**IMPORTANT:**
- **NEVER** include Claude Code attribution or co-author messages
- Focus on WHAT was implemented and WHY it matters

### Step 6: Write Comprehensive Tests

**CRITICAL: Use parallel test writing agents**

1. Identify all files that need unit tests
2. Launch **multiple Task agents in parallel** (one per test file) in a SINGLE message
3. Each agent should:
   - Write comprehensive unit tests for one specific file
   - Cover edge cases thoroughly
   - Aim for 80%+ code coverage
   - Follow existing test patterns in the codebase
   - Use appropriate testing frameworks (JUnit, Kotest, etc.)

### Step 6.5: User Review and Commit Tests

**CRITICAL: MUST get user confirmation before committing**

**This step follows `.claude/rules/git-workflow.md` rules strictly**

1. **Show test summary to user**:
   ```
   📊 Tests Complete

   Test files created:
   - app/src/test/.../DefaultDecksRepoTest.kt (new, 80 lines)
   - app/src/test/.../DeckStatsTest.kt (modified, +20 lines)

   Test execution:
   - ✅ All 15 tests pass
   - ✅ Code coverage: 87%

   Build: ✅ SUCCESS
   Lint: ✅ No violations

   Proposed commit message:
   test(repo): add tests for DefaultDecksRepo

   - Add comprehensive unit tests
   - Cover edge cases and error scenarios
   - Achieve 87% code coverage

   Refs #<ISSUE_NUMBER>
   ```

2. **Request user decision**:
   ```
   Proceed with commit?
   - Type "yes" to commit
   - Type "no" to skip
   - Type "modify" to change commit message
   ```

3. **Only after user approval**:
   ```bash
   git add .
   git commit -m "test(<scope>): add tests for <feature-name>

   <body>

   Refs #<ISSUE_NUMBER>"
   ```

**IMPORTANT:**
- ⛔ DO NOT auto-commit without user approval
- ✅ Use `test:` type for test-only commits
- ✅ **NEVER** include Claude Code attribution messages
- ✅ Describe what is being tested and coverage achieved

### Step 7: Validation

**CRITICAL: Run validation in parallel**

Launch **THREE Task agents in parallel** in a SINGLE message:

1. **Test Agent**: Run unit tests
   ```bash
   ./gradlew test
   ```

2. **Lint Agent**: Run linting
   ```bash
   ./gradlew ktlintCheck
   ```

3. **Build Agent**: Run build
   ```bash
   ./gradlew build
   ```

If any validation fails:
1. **Analyze the errors**
2. **Fix the issues**
3. **Show fixes summary to user**:
   ```
   🔧 Validation Fixes

   Issues found:
   - Lint error: Unused import in DefaultDecksRepo.kt
   - Test failure: Null pointer in DeckStatsTest.kt

   Fixed files:
   - app/.../DefaultDecksRepo.kt (modified, -1 line)
   - app/.../DeckStatsTest.kt (modified, +3 lines)

   Re-run validation:
   - Build: ✅ SUCCESS
   - Tests: ✅ 15/15 passed
   - Lint: ✅ No violations

   Proposed commit message:
   fix(repo): resolve lint and test issues

   - Remove unused import in DefaultDecksRepo
   - Fix null pointer exception in DeckStatsTest
   - Update mock data to match API response format

   Refs #<ISSUE_NUMBER>
   ```

4. **Request user confirmation**:
   ```
   Proceed with commit?
   - Type "yes" to commit
   - Type "no" to skip
   ```

5. **Only after user approval**, commit the fixes:
   ```bash
   git add .
   git commit -m "fix(<scope>): resolve <issue-description>

   <body>

   Refs #<ISSUE_NUMBER>"
   ```

6. **Re-run validation until all pass**

**CRITICAL**: Never skip user confirmation, even for small fixes

### Step 8: Create Pull Request

**Note:** All implementation, tests, and fixes should already be committed from previous steps.

1. Push the branch:
   ```bash
   git push -u origin <BRANCH_NAME>
   ```

   Where `<BRANCH_NAME>` is the descriptive branch name created in Step 2.

3. Create PR using `gh pr create` with language specified by `--lang` argument:

**Korean PR Template (`--lang ko`):**
```markdown
## 요약

[이슈 해결 내용 요약]

## 변경 사항

- [변경 사항 1]
- [변경 사항 2]
- [변경 사항 3]

## 구현 세부사항

[주요 구현 내용 설명]

## 테스트

- [ ] 유닛 테스트 작성 완료
- [ ] 테스트 통과 확인
- [ ] 린트 검사 통과
- [ ] 빌드 성공 확인

## 관련 이슈

Closes #<ISSUE_NUMBER>
```

**English PR Template (`--lang en`):**
```markdown
## Summary

[Summary of issue resolution]

## Changes

- [Change 1]
- [Change 2]
- [Change 3]

## Implementation Details

[Detailed explanation of implementation]

## Testing

- [ ] Unit tests written
- [ ] Tests passing
- [ ] Linting passing
- [ ] Build successful

## Related Issues

Closes #<ISSUE_NUMBER>
```

## 🎯 Usage Examples

```bash
# Solve issue #24 from main branch with Korean PR (all defaults)
/solve-issue --issue 24

# Solve issue #72 from dev branch with Korean PR
/solve-issue --issue 72 --base dev

# Solve issue #28 from dev branch with English PR
/solve-issue --issue 28 --base dev --lang en

# Solve issue #15 from main branch with English PR
/solve-issue --issue 15 --base main --lang en
```

## 🔍 Best Practices

1. **Parallel Execution**: Always launch multiple independent agents in a SINGLE message
2. **Thorough Research**: Don't skip codebase analysis - it ensures consistency
3. **Pattern Following**: Strictly adhere to existing code patterns in the codebase
4. **Comprehensive Testing**: Edge cases are critical - don't skip them
5. **Validation**: Never skip test/lint/build checks
6. **Incremental Commits**: Commit after each independent milestone (implementation, tests, fixes)
7. **Clean Commits**: Use Angular Git commit convention for all commit messages
8. **PR Quality**: Ensure PR description clearly explains what was done and why

## ⚠️ Critical Requirements

- **NEVER** commit directly to the base branch
- **ALWAYS** create a new descriptive branch for the issue
- **ALWAYS** launch parallel agents in a SINGLE message (not sequentially)
- **ALWAYS** get user confirmation BEFORE EACH commit (follows `.claude/rules/git-workflow.md`)
- **ALWAYS** run all validations before creating PR
- **ALWAYS** ensure tests achieve 80%+ coverage
- **ALWAYS** follow existing code patterns and architecture
- **ALWAYS** use Angular Git commit convention for all commits
- **NEVER** include Claude Code attribution or co-author messages in commits or PR
- **NEVER** auto-commit without explicit user approval

## 🚀 Execution

When this command is invoked:

1. Fetch issue #<ISSUE_NUMBER> using `gh issue view`
2. Generate descriptive branch name and create branch from `<BASE_BRANCH>`
3. Launch parallel research agents (up to 10, in ONE message)
4. Show scratchpad with implementation plan
5. Launch implementation agent
6. **Commit implementation** using Angular convention
7. Launch parallel test writing agents (in ONE message)
8. **Commit tests** using Angular convention
9. Launch parallel validation agents (test/lint/build in ONE message)
10. Fix any issues found during validation and **commit fixes**
11. Push branch and create PR in the specified language (`--lang`)
12. Output the PR URL

---

**Now, solve the GitHub issue following this exact workflow.**
