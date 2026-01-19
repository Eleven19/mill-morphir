# Testing Guide

This project employs two levels of testing: **Unit Tests** and **Integration Tests**.

## Unit Tests

Located in `plugin/test`, these tests verify the internal logic of the plugin components (e.g., `MorphirProjectConfig`) without spinning up a full Mill build.

- **Framework**: [Utest](https://github.com/com-lihaoyi/utest)
- **Running**:
  ```bash
  ./mill -i plugin.test.test
  ```

## Integration Tests

Located in `itest`, these tests verify that the plugin functions correctly when actually used in a Mill build. They use Mill's `IntegrationTester` to initialize a temporary workspace, load the plugin, and execute build tasks.

### Structure
The `itest` module points to test cases in `itest/src`. Currently, we have `basic-standalone`:
- `itest/src/basic-standalone/`: Contains the sources for the test project.
- `itest/src/basic-standalone/build.mill`: The build definition for the test project.
- `itest/src/basic-standalone/test/src/MorphirSpec.scala`: The test specification that drives the `IntegrationTester`.

### Environment Requirements
The integration tests simulate running `morphir` commands. Therefore, the test execution environment **must** have:
1.  **Node.js**: `node` and `npm` must be on the `$PATH`.
2.  **morphir-elm**: The test project performs an `npm install` to setup `morphir-elm` locally, but a global installation is also supported as a fallback.

### Adding a New Integration Test
1.  Create a new directory under `itest/src/` (e.g., `itest/src/my-new-case`).
2.  Add a `build.mill` and necessary source files to that directory.
3.  Add a new test specification file in `itest/src/basic-standalone/test/src/` (or restructure `itest` to support multiple keys if needed). *Currently, the setup uses a single source root for specs.*

### Running Integration Tests
```bash
./mill -i itest.test
```

## Continuous Integration
Our CI workflow (`.github/workflows/ci.yml`) automatically sets up Java and Node.js to ensure these tests pass.
