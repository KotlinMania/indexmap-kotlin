# port-lint Proposed Changes

**Generated:** 2026-05-24
**Source:** tmp/indexmap/src
**Target:** src/commonMain/kotlin/io/github/kotlinmania/indexmap

These are review proposals only. They are emitted when a Rust -> Kotlin pair matches only after fallback normalization, so the existing `port-lint` header is not an exact provenance match.

| Target file | Current header | Proposed header | Source path | Reason |
|-------------|----------------|-----------------|-------------|--------|
| `src/commonMain/kotlin/io/github/kotlinmania/indexmap/map/Typealiases.kt` | `// port-lint: source map.rs` | `// port-lint: source rayon/map.rs` | `rayon/map.rs` | `port-lint provenance header matched only by basename: 'map.rs' vs expected 'rayon/map.rs'` |
