# indexmap-kotlin in Kotlin

[![GitHub link](https://img.shields.io/badge/GitHub-KotlinMania%2Findexmap--kotlin-blue.svg)](https://github.com/KotlinMania/indexmap-kotlin)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.kotlinmania/indexmap-kotlin)](https://central.sonatype.com/artifact/io.github.kotlinmania/indexmap-kotlin)
[![Build status](https://img.shields.io/github/actions/workflow/status/KotlinMania/indexmap-kotlin/ci.yml?branch=main)](https://github.com/KotlinMania/indexmap-kotlin/actions)

This is a Kotlin Multiplatform line-by-line transliteration port of [`indexmap-rs/indexmap`](https://github.com/indexmap-rs/indexmap).

**Original Project:** This port is based on [`indexmap-rs/indexmap`](https://github.com/indexmap-rs/indexmap). All design credit and project intent belong to the upstream authors; this repository is a faithful port to Kotlin Multiplatform with no behavioural changes intended.

### Porting status

This is an **in-progress port**. The goal is feature parity with the upstream Rust crate while providing a native Kotlin Multiplatform API. Every Kotlin file carries a `// port-lint: source <path>` header naming its upstream Rust counterpart so the AST-distance tool can track provenance.

---

## Upstream README — `indexmap-rs/indexmap`

> The text below is reproduced and lightly edited from [`https://github.com/indexmap-rs/indexmap`](https://github.com/indexmap-rs/indexmap). It is the upstream project's own description and remains under the upstream authors' authorship; links have been rewritten to absolute upstream URLs so they continue to resolve from this repository.

## indexmap

[![build status](https://github.com/indexmap-rs/indexmap/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/indexmap-rs/indexmap/actions)
[![crates.io](https://img.shields.io/crates/v/indexmap.svg)](https://crates.io/crates/indexmap)
[![docs](https://docs.rs/indexmap/badge.svg)](https://docs.rs/indexmap)
[![rustc](https://img.shields.io/badge/rust-1.85%2B-orange.svg)](https://img.shields.io/badge/rust-1.85%2B-orange.svg)

A pure-Rust hash table which preserves (in a limited sense) insertion order.

This crate implements compact map and set data-structures,
where the iteration order of the keys is independent from their hash or
value. It preserves insertion order (except after removals), and it
allows lookup of entries by either hash table key or numerical index.

Note: this crate was originally released under the name `ordermap`,
but it was renamed to `indexmap` to better reflect its features.
The [`ordermap`](https://crates.io/crates/ordermap) crate now exists
as a wrapper over `indexmap` with stronger ordering properties.

# Background

This was inspired by Python 3.6's new dict implementation (which remembers
the insertion order and is fast to iterate, and is compact in memory).

Some of those features were translated to Rust, and some were not. The result
was indexmap, a hash table that has following properties:

- Order is **independent of hash function** and hash values of keys.
- Fast to iterate.
- Indexed in compact space.
- Preserves insertion order **as long** as you don't call `.remove()`,
  `.swap_remove()`, or other methods that explicitly change order.
  The alternate `.shift_remove()` does preserve relative order.
- Uses hashbrown for the inner table, just like Rust's libstd `HashMap` does.

## Performance

`IndexMap` derives a couple of performance facts directly from how it is constructed,
which is roughly:

> A raw hash table of key-value indices, and a vector of key-value pairs.

- Iteration is very fast since it is on the dense key-values.
- Removal is fast since it moves memory areas only in the table,
  and uses a single swap in the vector.
- Lookup is fast-ish because the initial 7-bit hash lookup uses SIMD, and indices are
  densely stored. Lookup also is slow-ish since the actual key-value pairs are stored
  separately. (Visible when cpu caches size is limiting.)

- In practice, `IndexMap` has been tested out as the hashmap in rustc in [PR45282] and
  the performance was roughly on par across the whole workload.
- If you want the properties of `IndexMap`, or its strongest performance points
  fits your workload, it might be the best hash table implementation.

[PR45282]: https://github.com/rust-lang/rust/pull/45282

# Recent Changes

See [RELEASES.md](https://github.com/indexmap-rs/indexmap/blob/main/RELEASES.md).

---

## About this Kotlin port

### Installation

```kotlin
dependencies {
    implementation("io.github.kotlinmania:indexmap-kotlin:0.1.2")
}
```

### Building

```bash
./gradlew build
./gradlew test
```

### Targets

- macOS arm64
- Linux x64
- Windows mingw-x64
- iOS arm64 / simulator-arm64 (Swift export + XCFramework)
- JS (browser + Node.js)
- Wasm-JS (browser + Node.js)
- Android (API 24+)

### Porting guidelines

See [AGENTS.md](AGENTS.md) for translator discipline, port-lint header convention, and Rust → Kotlin idiom mapping.

### License

This Kotlin port is distributed under the same Apache-2.0 license as the upstream [`indexmap-rs/indexmap`](https://github.com/indexmap-rs/indexmap). See [LICENSE](LICENSE) (and any sibling `LICENSE-*` / `NOTICE` files mirrored from upstream) for the full text.

Original work copyrighted by the indexmap authors.  
Kotlin port: Copyright (c) 2026 Sydney Renee and The Solace Project.

### Acknowledgments

Thanks to the [`indexmap-rs/indexmap`](https://github.com/indexmap-rs/indexmap) maintainers and contributors for the original Rust implementation. This port reproduces their work in Kotlin Multiplatform; bug reports about upstream design or behavior should go to the upstream repository.
