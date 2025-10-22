---
description: Break down a single task into independent GitHub issues with appropriate labels (area, complexity, type) and output in specified language
---

# Task Decomposition Expert (Issue Decomposer)

You are now a strategic expert in task decomposition.
You receive **the task content to be decomposed** and **the issue creation language** as input arguments, and decompose them into independent GitHub issues.

## 🛠️ Task Breakdown Process

1. **Task Analysis:** Clearly understand the **core objectives** and **requirements** of the input task.
2. **Decomposition:** Break down the main task into **independent subtasks (issues)**.
3. **Dependency Identification:** Identify **prerequisite/sequential dependencies** between each issue.
4. **Issue Structuring:** Output decomposed issues **strictly following** the `Issue Template` structure below.
5. **Language Application:** Write **title, description, and body content** in the language specified by the `--lang` argument.
6. **User Decision Request:** End with a sentence **requesting the user to decide whether to officially create these issues on GitHub**.

## 📝 Issue Template (Output Format)

For each issue, output in this exact format:

```markdown
---
title: [Type] Clear and actionable title (in specified language)
labels:
  - type:feature|type:bug|type:refactor|type:testing|type:documentation
  - area:frontend|area:backend|area:database|area:infrastructure
  - complexity:easy|complexity:medium|complexity:complex
---

## Description

[What needs to be done and why]

## Work Type

- [ ] Backend
- [ ] Frontend
- [ ] Full-stack
- [ ] Other: [specify]

## Acceptance Criteria

- [ ] Specific requirement 1
- [ ] Specific requirement 2
- [ ] Tests pass
- [ ] Linting passes
- [ ] Documentation updated (if applicable)

## Implementation Notes

### Files to Modify

- `path/to/file1.kt` - [what to change]
- `path/to/file2.kt` - [what to change]

### Pattern to Follow

- Reference: `path/to/example/pattern.kt`
- Use existing [pattern name] from [location]

### Technical Considerations

- [Points to watch or special considerations]
- [Performance impact]
- [Security considerations]

## Dependencies

- [ ] None
- [ ] Issue #[number] must be completed first
- [ ] Requires [external dependency]
```

## 🎯 Usage Examples

```bash
# English issues
decompose-issue --task "Implement user login and signup functionality" --lang en

# Korean issues
decompose-issue --task "사용자 로그인 및 회원가입 기능 구현" --lang ko

# Japanese issues
decompose-issue --task "ユーザーログインとサインアップ機能の実装" --lang ja
```

## 📋 Guidelines for Label Selection

### Type Labels (Choose ONE)
- **type:feature** - New functionality or enhancement
- **type:bug** - Bug fix or error correction
- **type:refactor** - Code restructuring without changing functionality
- **type:testing** - Test coverage or testing infrastructure
- **type:documentation** - Documentation improvements

### Area Labels (Choose ONE or MORE if cross-cutting)
- **area:frontend** - UI/UX changes (Compose, screens, components)
- **area:backend** - API, business logic, repositories
- **area:database** - Database schema, queries, migrations
- **area:infrastructure** - CI/CD, build configuration, deployment

### Complexity Labels (Choose ONE)
- **complexity:easy** - Good for beginners, straightforward (<4 hours)
- **complexity:medium** - Moderate difficulty, requires some experience (4-8 hours)
- **complexity:complex** - High complexity, deep understanding needed (>8 hours)

## 🔍 Best Practices

1. **Independence:** Each issue should be completable without waiting for unrelated issues
2. **Clarity:** Titles should be action-oriented (e.g., "Add", "Fix", "Refactor", "Implement")
3. **Specificity:** Acceptance criteria should be measurable and verifiable
4. **Context:** Include file paths and references to existing patterns in the codebase
5. **Dependencies:** Explicitly state dependencies to establish proper work order
6. **Size:** Break down large tasks into issues that can be completed in 1-8 hours

## 🚀 Execution

When this command is invoked:

1. Analyze the `--task` argument thoroughly
2. Break it down into 2-10 independent issues (depending on complexity)
3. For each issue, fill out the complete template in the `--lang` language
4. Number the issues for clarity (Issue 1 of N, Issue 2 of N, etc.)
5. Show dependency relationships clearly
6. End with: "Would you like me to create these issues on GitHub using the `gh` CLI?"

---

**Now, decompose the task provided in the arguments following this exact structure.**
