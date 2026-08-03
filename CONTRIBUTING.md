# Contributing

This is a personal learning project, but it's kept to the same workflow as a team project would be.

## Branching

- `main` is always kept buildable and passing CI.
- Work happens on short-lived branches named `type/short-description`, e.g. `feat/csv-export`,
  `fix/group-rename-bug`, `docs/readme-usage`.

## Commit messages

Follows [Conventional Commits](https://www.conventionalcommits.org/): `type(scope): summary`,
e.g. `fix(GroupManager): compare by reference on update`. Types used here: `feat`, `fix`, `docs`,
`test`, `refactor`, `chore`, `build`, `ci`.

## Pull requests

- Every change lands through a PR, even single-author ones — it's where CI runs and where the
  diff gets a final read before merging.
- CI (`.github/workflows/build.yml`) must pass: `mvn -B verify` (compiles + runs the test suite).
- Prefer small, single-purpose PRs over large ones — the Single Responsibility Principle applies
  to commits and PRs, not just classes.

## Tests

New behavior in `model/` should come with a test in `src/test/java`. See existing tests for the
style (one behavior per test, descriptive method names, no shared mutable fixtures beyond
`@BeforeEach`).
