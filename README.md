# mill-morphir

A [Mill](https://mill-build.org/) plugin for working with [Morphir](https://github.com/finos/morphir) projects and workspaces.

## Compatibility

| Mill Version | mill-morphir Version |
|--------------|----------------------|
| 1.0.6+       | 0.0.1-SNAPSHOT       |

## Installation

Add the following to your `build.mill` (or `build.sc`):

```scala
import $ivy.`io.eleven19.mill::mill-morphir::0.0.1-SNAPSHOT`
import io.eleven19.mill.morphir._
```

> **Note:** Since this is a Snapshot version, you may need to add the Sonatype Snapshots repository or publish locally first.

## Usage

This plugin provides modules to integrate Morphir tools (like `morphir-elm`) into your Mill build pipeline.

### Morphir Module

Extend `MorphirModule` to add Morphir capabilities to your module.

```scala
object myproject extends MorphirModule {
  // Configuration
}
```

## Development

### Prerequisites

- **Java**: 11 or higher (Java 17+ recommended).
- **Mill**: 1.0.6 (handled via `./mill` script).
- **Node.js**: Required for `morphir-elm` execution during tests.
- **morphir-elm**: Install globally via `npm install -g morphir-elm` or ensure it is available in your workspace `node_modules`.

### Build

Compile the project:

```bash
./mill -i __.compile
```

### Testing

Run unit tests:

```bash
./mill -i plugin.test.test
```

Run integration tests (requires `morphir-elm`):

```bash
./mill -i itest.test
```

### formatting

Check format:
```bash
./mill -i __.checkFormat
```

Reformat:
```bash
./mill -i __.reformat
```
