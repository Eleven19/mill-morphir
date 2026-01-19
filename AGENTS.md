# Agentic Guidance for mill-morphir

This document provides context, conventions, and resources for AI agents working on the `mill-morphir` repository.

## Project Context
- **Project Type**: Mill Plugin (Scala).
- **Mill Version**: 1.0.6 (Strict requirement).
- **Scala Version**: 3.7.3 (targeting TASTy compatibility with Mill 1.x).
- **Primary Goal**: Enable Morphir tooling integration (Elm, IR processing) within Mill builds.

## Repository Structure
- `build.mill`: Main build definition (Mill 1.x convention, formerly `build.sc`).
- `plugin/`: Source code for the plugin itself.
  - `src/`: Main source code (`MorphirModule`, etc.).
  - `test/`: Unit tests using **Utest**.
- `itest/`: Integration testing module.
  - `src/basic-standalone/`: A standalone Mill project used to test the plugin in a real execution environment.
- `.github/workflows/`: CI/CD automation (`ci.yml`, `release.yml`). Workflows use `jdx/mise-action` for tool consistency.
- `mise.toml`: Tool version pinning and task definitions.
- `.mise/tasks/`: Shell script tasks (e.g., `release`).
- `cliff.toml`: Configuration for `git-cliff` changelog generation.

## Development Conventions

### Mill 1.x Migration
We have fully migrated to Mill 1.x. Respect these API changes:
- **Task Definition**: Use `Task` type and `Task {}` macro instead of `Target`/`T`.
- **Paths**: Use `moduleDir` instead of `millSourcePath`.
- **Dependencies**: Use `mvn"group::artifact:version"` syntax.
- **Commands**: Use `Task.Command` for runnable tasks.

### Testing
- **Unit Tests**: Located in `plugin/test`. Use `utest`.
- **Integration Tests**: Located in `itest`.
  - Use `mill.testkit.IntegrationTester` to spin up a sandbox.
  - The `itest` module explicitly depends on `mill-testkit`.
  - Tests verify that the plugin can load and execute `morphir` commands in a subprocess.
  - **Critical**: Integration tests depend on `npm` and `morphir-elm` being available in the environment.


### Development Environment & Tooling
- **Mise**: We use [mise](https://mise.jdx.dev/) to manage development tools (`java` (Temurin 17), `node` (20), `git-cliff`).
- **Tasks**: Common workflows are defined in `mise.toml` and `.mise/tasks/`:
    - `mise run build`: Compiles the project.
    - `mise run test`: Runs all tests (Unit + Integration).
    - `mise run release <version>`: Automated release script (changelog generation -> tag -> GitHub release).
- **Mill Bootstrap**: Mill itself is managed via the `./mill` bootstrap script, not Mise.

### External Tools
- **Node.js**: Required for `morphir-elm` CLI. Managed via Mise.
- **git-cliff**: Used for generating `CHANGELOG.md` based on conventional commits. Managed via Mise.

## Resources & Documentation

- **Mill Documentation**: [https://mill-build.org/mill/Intro_to_Mill.html](https://mill-build.org/mill/Intro_to_Mill.html)
- **Mill Plugin Development**: [https://mill-build.org/mill/Extending_Mill.html](https://mill-build.org/mill/Extending_Mill.html)
- **Mill Publishing**: [https://mill-build.org/mill/Publishing_Java_Scale_Projects.html](https://mill-build.org/mill/Publishing_Java_Scale_Projects.html)
- **Morphir**: [https://github.com/finos/morphir](https://github.com/finos/morphir)
- **Mise**: [https://mise.jdx.dev/](https://mise.jdx.dev/)
