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
3. Create a new branch named `issue-<ISSUE_NUMBER>` (e.g., `issue-72`)
4. Switch to the new branch

**Commands:**
```bash
git checkout <BASE_BRANCH>
git pull origin <BASE_BRANCH>
git checkout -b issue-<ISSUE_NUMBER>
```

Where `<BASE_BRANCH>` is the value from `--base` argument (default: `main`)

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
- Analyze the errors
- Fix the issues
- Re-run validation until all pass

### Step 8: Create Pull Request

1. Stage and commit all changes:
   ```bash
   git add .
   git commit -m "[Appropriate commit message]"
   ```

2. Push the branch:
   ```bash
   git push -u origin issue-<ISSUE_NUMBER>
   ```

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

🤖 Generated with [Claude Code](https://claude.com/claude-code)
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

🤖 Generated with [Claude Code](https://claude.com/claude-code)
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
6. **Clean Commits**: Write clear, descriptive commit messages
7. **PR Quality**: Ensure PR description clearly explains what was done and why

## ⚠️ Critical Requirements

- **NEVER** commit directly to the base branch
- **ALWAYS** create a new branch for the issue
- **ALWAYS** launch parallel agents in a SINGLE message (not sequentially)
- **ALWAYS** run all validations before creating PR
- **ALWAYS** ensure tests achieve 80%+ coverage
- **ALWAYS** follow existing code patterns and architecture

## 🚀 Execution

When this command is invoked:

1. Fetch issue #<ISSUE_NUMBER> using `gh issue view`
2. Create and switch to `issue-<ISSUE_NUMBER>` branch from `<BASE_BRANCH>`
3. Launch parallel research agents (up to 10, in ONE message)
4. Show scratchpad with implementation plan
5. Launch implementation agent
6. Launch parallel test writing agents (in ONE message)
7. Launch parallel validation agents (test/lint/build in ONE message)
8. Fix any issues found during validation
9. Commit, push, and create PR in the specified language (`--lang`)
10. Output the PR URL

---

**Now, solve the GitHub issue following this exact workflow.**
