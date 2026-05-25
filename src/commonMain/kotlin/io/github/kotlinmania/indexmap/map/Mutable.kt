@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

// port-lint: source map/mutable.rs
package io.github.kotlinmania.indexmap.map

import kotlin.native.HiddenFromObjC

// Opt-in mutable access to IndexMap keys.
@HiddenFromObjC
public interface MutableKeys<K, V> {
    public fun getFullMut2(key: K): Triple<Int, K, V>?

    public fun getIndexMut2(index: Int): Pair<K, V>?

    public fun iterMut2(): List<Pair<K, V>>

    public fun retain2(keep: (K, V) -> Boolean)
}

// Opt-in mutable access to Entry keys.
@HiddenFromObjC
public interface MutableEntryKey<K> {
    public fun key(): K

    public fun replaceKey(newKey: K): K
}
