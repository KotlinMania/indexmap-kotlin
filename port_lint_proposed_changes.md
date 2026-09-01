# port-lint Proposed Changes

**Generated:** 2026-08-31
**Source:** tmp/indexmap/src
**Target:** src

These are review proposals only. They are emitted when a Rust -> Kotlin pair matches only after fallback normalization, so the existing `port-lint` header is not an exact provenance match.

| Target file | Current header | Proposed header | Source path | Reason |
|-------------|----------------|-----------------|-------------|--------|
| `commonTest/kotlin/io/github/kotlinmania/indexmap/TestsTest.kt` | `// port-lint: tests tests.rs` | `// port-lint: tests map/tests.rs` | `map/tests.rs` | `port-lint provenance header matched only by basename: 'tests:tests.rs' vs expected 'map/tests.rs'` |
