# port-lint Proposed Changes

**Generated:** 2026-08-23
**Source:** tmp/indexmap
**Target:** src/commonMain/kotlin

These are review proposals only. They are emitted when a Rust -> Kotlin pair matches only after fallback normalization, so the existing `port-lint` header is not an exact provenance match.

| Target file | Current header | Proposed header | Source path | Reason |
|-------------|----------------|-----------------|-------------|--------|
| `src/commonMain/kotlin/io/github/kotlinmania/indexmap/map/Slice.kt` | `// port-lint: source map/slice.rs` | `// port-lint: source map/slice.rs` | `map/slice.rs` | `port-lint provenance header matched only after fallback normalization: 'map/slice.rs' vs expected 'map/slice.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/indexmap/map/SliceTest.kt` | `// port-lint: tests map/slice.rs` | `// port-lint: tests map/slice.rs` | `map/slice.rs` | `port-lint provenance header matched only after fallback normalization: 'tests:map/slice.rs' vs expected 'map/slice.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/indexmap/map/Entry.kt` | `// port-lint: source map/entry.rs` | `// port-lint: source map/entry.rs` | `map/entry.rs` | `port-lint provenance header matched only after fallback normalization: 'map/entry.rs' vs expected 'map/entry.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/indexmap/map/Typealiases.kt` | `// port-lint: source map.rs` | `// port-lint: source rayon/map.rs` | `rayon/map.rs` | `port-lint provenance header matched only by basename: 'map.rs' vs expected 'rayon/map.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/indexmap/set/Slice.kt` | `// port-lint: source set/slice.rs` | `// port-lint: source set/slice.rs` | `set/slice.rs` | `port-lint provenance header matched only after fallback normalization: 'set/slice.rs' vs expected 'set/slice.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/indexmap/set/SliceTest.kt` | `// port-lint: tests set/slice.rs` | `// port-lint: tests set/slice.rs` | `set/slice.rs` | `port-lint provenance header matched only after fallback normalization: 'tests:set/slice.rs' vs expected 'set/slice.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/indexmap/map/Iter.kt` | `// port-lint: source map/iter.rs` | `// port-lint: source map/iter.rs` | `map/iter.rs` | `port-lint provenance header matched only after fallback normalization: 'map/iter.rs' vs expected 'map/iter.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/indexmap/map/IterTest.kt` | `// port-lint: tests map/iter.rs` | `// port-lint: tests map/iter.rs` | `map/iter.rs` | `port-lint provenance header matched only after fallback normalization: 'tests:map/iter.rs' vs expected 'map/iter.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/indexmap/set/Iter.kt` | `// port-lint: source set/iter.rs` | `// port-lint: source set/iter.rs` | `set/iter.rs` | `port-lint provenance header matched only after fallback normalization: 'set/iter.rs' vs expected 'set/iter.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/indexmap/set/IterTest.kt` | `// port-lint: tests set/iter.rs` | `// port-lint: tests set/iter.rs` | `set/iter.rs` | `port-lint provenance header matched only after fallback normalization: 'tests:set/iter.rs' vs expected 'set/iter.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/indexmap/map/Mutable.kt` | `// port-lint: source map/mutable.rs` | `// port-lint: source map/mutable.rs` | `map/mutable.rs` | `port-lint provenance header matched only after fallback normalization: 'map/mutable.rs' vs expected 'map/mutable.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/indexmap/set/Mutable.kt` | `// port-lint: source set/mutable.rs` | `// port-lint: source set/mutable.rs` | `set/mutable.rs` | `port-lint provenance header matched only after fallback normalization: 'set/mutable.rs' vs expected 'set/mutable.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/indexmap/Lib.kt` | `// port-lint: source lib.rs` | `// port-lint: source lib.rs` | `lib.rs` | `port-lint provenance header matched only after fallback normalization: 'lib.rs' vs expected 'lib.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/indexmap/LibTest.kt` | `// port-lint: tests lib.rs` | `// port-lint: tests lib.rs` | `lib.rs` | `port-lint provenance header matched only after fallback normalization: 'tests:lib.rs' vs expected 'lib.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/indexmap/Set.kt` | `// port-lint: source set.rs` | `// port-lint: source set.rs` | `set.rs` | `port-lint provenance header matched only after fallback normalization: 'set.rs' vs expected 'set.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/indexmap/SetTest.kt` | `// port-lint: tests set.rs` | `// port-lint: tests set.rs` | `set.rs` | `port-lint provenance header matched only after fallback normalization: 'tests:set.rs' vs expected 'set.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/indexmap/Map.kt` | `// port-lint: source map.rs` | `// port-lint: source map.rs` | `map.rs` | `port-lint provenance header matched only after fallback normalization: 'map.rs' vs expected 'map.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/indexmap/MapTest.kt` | `// port-lint: tests map.rs` | `// port-lint: tests map.rs` | `map.rs` | `port-lint provenance header matched only after fallback normalization: 'tests:map.rs' vs expected 'map.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/indexmap/Util.kt` | `// port-lint: source util.rs` | `// port-lint: source util.rs` | `util.rs` | `port-lint provenance header matched only after fallback normalization: 'util.rs' vs expected 'util.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/indexmap/UtilTest.kt` | `// port-lint: tests util.rs` | `// port-lint: tests util.rs` | `util.rs` | `port-lint provenance header matched only after fallback normalization: 'tests:util.rs' vs expected 'util.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/indexmap/Macros.kt` | `// port-lint: source macros.rs` | `// port-lint: source macros.rs` | `macros.rs` | `port-lint provenance header matched only after fallback normalization: 'macros.rs' vs expected 'macros.rs'` |
