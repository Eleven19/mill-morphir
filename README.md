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

We use **[mise](https://mise.jdx.dev/)** to manage our development tools.

1.  Install `mise`.
2.  Run `mise install` to set up specific versions of `java`, `node`, and utilities.

Alternatively, manually install:
- **Java**: 17+
- **Mill**: 1.0.6 (or use `./mill`)
- **Node.js**: 20+
- **morphir-elm**: `npm install -g morphir-elm`

### Common Tasks (via Mise)

```bash
mise run build      # Compile
mise run test       # Run all tests
mise run release    # Trigger release
```

### Manual Commands

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
