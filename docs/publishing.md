# Publishing Guide

This project publishes artifacts to **Sonatype Maven Central** using the new Sonatype Central Portal APIs (supported by Mill 1.x `SonatypeCentralPublishModule`).

## Prerequisites

To publish, you must have the following secrets configured in the GitHub repository:

- `ELEVEN19_SONATYPE_USERNAME`: Your Sonatype Central token username.
- `ELEVEN19_SONATYPE_PASSWORD`: Your Sonatype Central token password.
- `ELEVEN19_IO_PGP_SECRET_BASE64`: Your PGP private key, base64 encoded.
- `ELEVEN19_IO_PGP_PASSPHRASE`: The passphrase for your PGP key.

> **Note**: The CI workflow sanitizes the Base64 secret to remove newlines before passing it to Mill.

## Versioning

Versioning is handled dynamically based on **Git Tags**.
- The `build.mill` defines `gitHeadVersion`, which runs `git describe --tags --always --dirty`.
- **To release a new version**, simply push a tag:
  ```bash
  git tag v0.1.0
  git push origin v0.1.0
  ```

## Release Workflow

The automation is defined in `.github/workflows/release.yml`.

1.  **Trigger**: The workflow runs on pushes to `main` and on tag pushes.
2.  **Steps**:
    - Checkout code.
    - Setup Java 17 and Node.js 20.
    - Install `morphir-elm` (required for build/test sanity check).
    - **Publish**: Executes `./mill -i __.publishSonatypeCentral`.
    - **Environment Injection**: Accesses GitHub Secrets and maps them to the `MILL_` prefixed environment variables required by the plugin:
        - `MILL_SONATYPE_USERNAME`
        - `MILL_SONATYPE_PASSWORD`
        - `MILL_PGP_SECRET_BASE64`
        - `MILL_PGP_PASSPHRASE`

## Manual Publishing (Local)

If you need to publish from a local machine:

1.  Export the required environment variables:
    ```bash
    export MILL_SONATYPE_USERNAME=...
    export MILL_SONATYPE_PASSWORD=...
    export MILL_PGP_SECRET_BASE64=...
    export MILL_PGP_PASSPHRASE=...
    ```
2.  Run the publish task:
    ```bash
    ./mill -i __.publishSonatypeCentral
    ```
