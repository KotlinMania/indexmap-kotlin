// port-lint: source rayon/mod.rs
package io.github.kotlinmania.indexmap.rayon

/*
 * Parallel iterator types for IndexMap and IndexSet with rayon.
 *
 * Translation ledger for `src/rayon/mod.rs`.
 *
 * Upstream submodules:
 *   - map -> Map.kt
 *   - set -> Set.kt
 */

internal fun <T> collect(sequence: Sequence<T>): List<T> = sequence.toList()
